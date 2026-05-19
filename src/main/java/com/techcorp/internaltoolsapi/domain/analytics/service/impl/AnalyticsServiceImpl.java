package com.techcorp.internaltoolsapi.domain.analytics.service.impl;

import com.techcorp.internaltoolsapi.api.exception.InvalidAnalyticsParameterException;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostSummaryResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.usage.UsageMetricsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.usage.UsagePeriodMetricsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.entity.enums.DepartmentCostSortField;
import com.techcorp.internaltoolsapi.domain.analytics.repository.AnalyticsRepository;
import com.techcorp.internaltoolsapi.domain.analytics.repository.UsageLogRepository;
import com.techcorp.internaltoolsapi.domain.analytics.service.AnalyticsService;
import com.techcorp.internaltoolsapi.domain.tools.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.domain.tools.entity.enums.ToolStatusType;
import com.techcorp.internaltoolsapi.shared.numeric.NumericService;
import com.techcorp.internaltoolsapi.shared.sort.SortDirection;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Default analytics service implementation.
 */
@Service
public class AnalyticsServiceImpl
        implements AnalyticsService {

    // ---------- DEPENDENCIES ---------- //

    private final UsageLogRepository usageLogRepository;
    private final AnalyticsRepository analyticsRepository;
    private final NumericService numericService;

    // ---------- CONSTRUCTOR ---------- //

    public AnalyticsServiceImpl(
            UsageLogRepository usageLogRepository,
            AnalyticsRepository analyticsRepository,
            NumericService numericService
    ) {

        this.usageLogRepository = usageLogRepository;
        this.analyticsRepository = analyticsRepository;
        this.numericService = numericService;
    }

    // ---------- PUBLIC METHODS ---------- //

    @Override
    public UsageMetricsResponse getUsageMetrics(
            Integer toolId
    ) {

        Object[] result =
                usageLogRepository.getUsageMetrics(
                        toolId,
                        LocalDate.now().minusDays(30)
                );

        Object[] metrics =
                (Object[]) result[0];
        long totalSessions = metrics[0] != null ? ((Number) metrics[0]).longValue() : 0L;

        double avgSessionMinutes =
                metrics[1] != null ? ((Number) metrics[1]).doubleValue() : 0.0;

        UsagePeriodMetricsResponse last30Days =
                new UsagePeriodMetricsResponse(
                        (int) totalSessions,
                        (int) Math.round(avgSessionMinutes)
                );

        return new UsageMetricsResponse(last30Days);
    }

    /**
     * Retrieves department cost analytics.
     * <p>
     * Analytics include:
     * - department cost aggregation
     * - active tools only
     * - company-level summary
     * - cost distribution percentages
     * - defensive department completion
     *
     * @param sortBy optional sort field
     * @param order  optional sort direction
     * @return department costs analytics
     */
    @Override
    public DepartmentCostsResponse getDepartmentCosts(
            String sortBy,
            String order
    ) {
        DepartmentCostSortField sortField =
                parseDepartmentCostSortField(
                        sortBy
                );

        SortDirection direction =
                parseSortDirection(
                        order
                );

        List<Object[]> results =
                analyticsRepository.getDepartmentCosts(
                        ToolStatusType.active,
                        sortField,
                        direction
                );

        if (results.isEmpty()) {

            return buildEmptyDepartmentCostsResponse();
        }

        List<DepartmentCostResponse> data =
                ensureAllDepartmentsPresent(
                        buildDepartmentCostData(
                                results
                        ),
                        sortField,
                        direction
                );

        DepartmentCostSummaryResponse summary =
                buildDepartmentCostSummary(data);

        applyDepartmentCostPercentages(
                data,
                summary.getTotalCompanyCost()
        );

        return new DepartmentCostsResponse(data, summary);
    }

    // ---------- PRIVATE METHODS ---------- //

    /**
     * Builds department-level analytics data
     * from raw repository aggregation results.
     *
     * @param results repository aggregation results
     * @return department analytics data
     */
    private List<DepartmentCostResponse> buildDepartmentCostData(
            List<Object[]> results
    ) {

        List<DepartmentCostResponse> data =
                new ArrayList<>();

        for (Object[] row : results) {

            DepartmentCostResponse response =
                    new DepartmentCostResponse(
                            row[0] != null ? row[0].toString() : null,
                            numericService.roundMoney(
                                    row[1] != null
                                            ? (BigDecimal) row[1]
                                            : BigDecimal.ZERO
                            ),
                            row[2] != null ? ((Number) row[2]).intValue() : 0,
                            row[3] != null ? ((Number) row[3]).intValue() : 0,
                            numericService.roundMoney(
                                    row[4] != null
                                            ? BigDecimal.valueOf(
                                            ((Number) row[4]).doubleValue()
                                    )
                                            : BigDecimal.ZERO
                            ),

                            null // Calculated later via applyDepartmentCostPercentages()
                    );

            data.add(response);
        }

        return data;
    }

    /**
     * Builds company-level summary
     * for department cost analytics.
     *
     * @param data department analytics data
     * @return summary response
     */
    private DepartmentCostSummaryResponse buildDepartmentCostSummary(
            List<DepartmentCostResponse> data
    ) {

        BigDecimal totalCompanyCost = BigDecimal.ZERO;
        DepartmentCostResponse mostExpensiveDepartment = null;

        for (DepartmentCostResponse department : data) {

            BigDecimal departmentCost = department.getTotalCost();

            totalCompanyCost =
                    totalCompanyCost.add(departmentCost);

            if (mostExpensiveDepartment == null
                    || departmentCost.compareTo(
                    mostExpensiveDepartment.getTotalCost()
            ) > 0) {

                mostExpensiveDepartment =
                        department;
            }
        }

        return new DepartmentCostSummaryResponse(
                totalCompanyCost,
                data.size(),
                mostExpensiveDepartment != null
                        ? mostExpensiveDepartment.getDepartment()
                        : null
        );
    }

    /**
     * Applies department cost
     * distribution percentages.
     * <p>
     * Percentages are calculated
     * against total company cost.
     *
     * @param data             department analytics data
     * @param totalCompanyCost total company cost
     */
    private void applyDepartmentCostPercentages(
            List<DepartmentCostResponse> data,
            BigDecimal totalCompanyCost
    ) {

        if (totalCompanyCost.compareTo(BigDecimal.ZERO) == 0) {

            return;
        }

        for (DepartmentCostResponse department : data) {

            double percentage =
                    numericService.roundPercentage(
                            department.getTotalCost()
                                    .multiply(BigDecimal.valueOf(100))
                                    .divide(
                                            totalCompanyCost,
                                            4,
                                            java.math.RoundingMode.HALF_UP
                                    )
                    );

            department.setCostPercentage(
                    percentage
            );
        }
    }

    /**
     * Parses requested department
     * analytics sort field.
     * <p>
     * Defaults to department.
     *
     * @param sortBy requested sort field
     * @return resolved sort field
     */
    private DepartmentCostSortField parseDepartmentCostSortField(
            String sortBy
    ) {

        if (sortBy == null
                || sortBy.isBlank()) {

            return DepartmentCostSortField.department;
        }

        try {

            return DepartmentCostSortField.valueOf(
                    sortBy.toLowerCase()
            );

        } catch (IllegalArgumentException ex) {

            throw new InvalidAnalyticsParameterException(
                    "Invalid sort_by parameter. Allowed values: department, total_cost."
            );
        }
    }

    /**
     * Parses requested
     * sort direction.
     * <p>
     * Defaults to asc.
     *
     * @param order requested direction
     * @return resolved direction
     */
    private SortDirection parseSortDirection(
            String order
    ) {

        if (order == null
                || order.isBlank()) {

            return SortDirection.asc;
        }

        try {

            return SortDirection.valueOf(
                    order.toLowerCase()
            );

        } catch (IllegalArgumentException ex) {

            throw new InvalidAnalyticsParameterException(
                    "Invalid order parameter. Allowed values: asc, desc."
            );
        }
    }

    /**
     * Ensures all departments are present
     * in analytics response.
     * <p>
     * Departments without active tools
     * are injected with zero metrics.
     * <p>
     * Final defensive sorting is applied
     * after synthetic department insertion
     * to preserve API sorting contract.
     *
     * @param data      existing analytics rows
     * @param sortField requested sort field
     * @param direction requested sort direction
     * @return completed department analytics
     */
    private List<DepartmentCostResponse> ensureAllDepartmentsPresent(
            List<DepartmentCostResponse> data,
            DepartmentCostSortField sortField,
            SortDirection direction
    ) {

        Map<DepartmentType, DepartmentCostResponse> departmentsMap =
                new LinkedHashMap<>();

        for (DepartmentCostResponse response : data) {

            departmentsMap.put(
                    DepartmentType.valueOf(
                            response.getDepartment()
                    ),
                    response
            );
        }

        for (DepartmentType department : DepartmentType.values()) {

            departmentsMap.putIfAbsent(
                    department,
                    buildEmptyDepartmentResponse(
                            department
                    )
            );
        }

        return sortDepartmentCostData(
                new ArrayList<>(
                        departmentsMap.values()
                ),
                sortField,
                direction
        );
    }

    /**
     * Builds empty department
     * analytics response.
     *
     * @param department department
     * @return zero-valued response
     */
    private DepartmentCostResponse buildEmptyDepartmentResponse(
            DepartmentType department
    ) {

        return new DepartmentCostResponse(
                department.name(),
                BigDecimal.ZERO,
                0,
                0,
                BigDecimal.ZERO,
                0.0
        );
    }

    /**
     * Builds empty department
     * costs analytics response.
     * <p>
     * Used when no active analytics
     * data exists.
     *
     * @return empty analytics response
     */
    private DepartmentCostsResponse buildEmptyDepartmentCostsResponse() {

        return new DepartmentCostsResponse(
                Collections.emptyList(),
                "No analytics data available - ensure tools data exists",
                new DepartmentCostSummaryResponse(
                        BigDecimal.ZERO,
                        0,
                        null
                )
        );
    }

    /**
     * Applies defensive sorting
     * after synthetic department insertion.
     * <p>
     * Ensures API sorting contract
     * remains preserved after
     * analytics dataset mutation.
     *
     * @param data      department analytics
     * @param sortField sort field
     * @param direction sort direction
     * @return sorted data
     */
    private List<DepartmentCostResponse> sortDepartmentCostData(
            List<DepartmentCostResponse> data,
            DepartmentCostSortField sortField,
            SortDirection direction
    ) {

        Comparator<DepartmentCostResponse> comparator =
                switch (sortField) {

                    case total_cost -> Comparator.comparing(
                            DepartmentCostResponse::getTotalCost
                    );

                    case department -> Comparator.comparing(
                            DepartmentCostResponse::getDepartment
                    );
                };

        if (direction == SortDirection.desc) {

            comparator = comparator.reversed();
        }

        return data.stream()
                .sorted(comparator)
                .toList();
    }
}