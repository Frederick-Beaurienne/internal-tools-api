package com.techcorp.internaltoolsapi.domain.analytics.service.impl;

import com.techcorp.internaltoolsapi.api.exception.InvalidAnalyticsParameterException;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostSummaryResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.expensivetools.ExpensiveToolResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.expensivetools.ExpensiveToolsAnalysisResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.expensivetools.ExpensiveToolsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.usage.UsageMetricsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.usage.UsagePeriodMetricsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.entity.enums.DepartmentCostSortField;
import com.techcorp.internaltoolsapi.domain.analytics.enums.EfficiencyRating;
import com.techcorp.internaltoolsapi.domain.analytics.mapper.AnalyticsMapper;
import com.techcorp.internaltoolsapi.domain.analytics.repository.AnalyticsRepository;
import com.techcorp.internaltoolsapi.domain.analytics.repository.UsageLogRepository;
import com.techcorp.internaltoolsapi.domain.analytics.service.AnalyticsService;
import com.techcorp.internaltoolsapi.domain.tools.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.domain.tools.entity.enums.ToolStatusType;
import com.techcorp.internaltoolsapi.shared.numeric.NumericService;
import com.techcorp.internaltoolsapi.shared.sort.SortDirection;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public ExpensiveToolsResponse getExpensiveTools(
            BigDecimal minCost,
            Integer limit
    ) {

        Integer validatedLimit =
                validateAnalyticsLimit(
                        limit
                );

        List<Object[]> results =
                analyticsRepository.findExpensiveTools(
                        minCost,
                        PageRequest.of(
                                0,
                                validatedLimit
                        )
                );

        if (results.isEmpty()) {

            return buildEmptyExpensiveToolsResponse();
        }

        BigDecimal companyAvg =
                calculateCompanyAverageCostPerUser(
                        analyticsRepository
                                .findActiveToolsForCompanyAverage()
                );

        List<ExpensiveToolResponse> data =
                buildExpensiveToolsData(
                        results,
                        companyAvg
                );

        return new ExpensiveToolsResponse(
                data,
                buildExpensiveToolsAnalysis(
                        data,
                        companyAvg
                )
        );
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

            data.add(
                    AnalyticsMapper.toDepartmentCostResponse(
                            row,
                            numericService
                    )
            );
        }

        return data;
    }

    /**
     * Validates analytics limit.
     *
     * @param limit requested limit
     * @return validated limit
     */
    private Integer validateAnalyticsLimit(
            Integer limit
    ) {

        if (limit == null) {
            return 10;
        }

        if (limit < 1 || limit > 100) {

            throw new InvalidAnalyticsParameterException(
                    "Invalid analytics parameter",
                    Map.of(
                            "limit",
                            "Must be positive integer between 1 and 100"
                    )
            );
        }

        return limit;
    }

    /**
     * Calculates company-wide
     * average tool cost per user.
     * <p>
     * Benchmark is computed as
     * a company-wide weighted
     * average:
     * SUM(monthly_cost)
     * / SUM(active_users_count)
     * <p>
     * Tools with zero users
     * are excluded and handled
     * separately through
     * not_applicable
     * efficiency rating.
     *
     * @param results repository results
     * @return company average cpu
     */
    private BigDecimal calculateCompanyAverageCostPerUser(
            List<Object[]> results
    ) {

        BigDecimal totalCost =
                BigDecimal.ZERO;

        int totalUsers = 0;

        for (Object[] row : results) {

            BigDecimal cost =
                    row[0] != null
                            ? (BigDecimal) row[0]
                            : BigDecimal.ZERO;

            int users =
                    row[1] != null
                            ? ((Number) row[1]).intValue()
                            : 0;

            if (users > 0) {

                totalCost =
                        totalCost.add(cost);

                totalUsers += users;
            }
        }

        if (totalUsers == 0) {
            return BigDecimal.ZERO;
        }

        return numericService.roundMoney(
                totalCost.divide(
                        BigDecimal.valueOf(
                                totalUsers
                        ),
                        4,
                        java.math.RoundingMode.HALF_UP
                )
        );
    }

    /**
     * Builds expensive tools data.
     * <p>
     * Efficiency rating rules:
     * - calculated against
     *   company average cpu
     * - tools with zero users
     *   receive
     *   not_applicable
     *   to avoid misleading
     *   efficiency scoring
     *
     * @param results repository results
     * @param companyAvg company average cpu
     * @return analytics data
     */
    private List<ExpensiveToolResponse> buildExpensiveToolsData(
            List<Object[]> results,
            BigDecimal companyAvg
    ) {

        List<ExpensiveToolResponse> data =
                new ArrayList<>();

        for (Object[] row : results) {

            BigDecimal cost =
                    row[2] != null
                            ? (BigDecimal) row[2]
                            : BigDecimal.ZERO;

            int users =
                    row[3] != null
                            ? ((Number) row[3]).intValue()
                            : 0;

            BigDecimal costPerUser =
                    users > 0
                            ? numericService.roundMoney(
                            cost.divide(
                                    BigDecimal.valueOf(
                                            users
                                    ),
                                    4,
                                    java.math.RoundingMode.HALF_UP
                            )
                    )
                            : BigDecimal.ZERO;

            EfficiencyRating rating =
                    users == 0
                            ? EfficiencyRating.not_applicable
                            : resolveEfficiencyRating(
                            costPerUser,
                            companyAvg
                    );

            data.add(
                    AnalyticsMapper.toExpensiveToolResponse(
                            row,
                            costPerUser,
                            rating,
                            numericService
                    )
            );
        }

        return data;
    }

    /**
     * Resolves efficiency rating.
     *
     * @param cpu tool cpu
     * @param companyAvg company avg cpu
     * @return rating
     */
    private EfficiencyRating resolveEfficiencyRating(
            BigDecimal cpu,
            BigDecimal companyAvg
    ) {

        if (companyAvg.compareTo(
                BigDecimal.ZERO
        ) == 0) {

            return EfficiencyRating.average;
        }

        BigDecimal ratio =
                cpu.divide(
                        companyAvg,
                        4,
                        java.math.RoundingMode.HALF_UP
                );

        if (ratio.compareTo(
                BigDecimal.valueOf(
                        0.5
                )
        ) < 0) {

            return EfficiencyRating.excellent;
        }

        if (ratio.compareTo(
                BigDecimal.valueOf(
                        0.8
                )
        ) < 0) {

            return EfficiencyRating.good;
        }

        if (ratio.compareTo(
                BigDecimal.valueOf(
                        1.2
                )
        ) <= 0) {

            return EfficiencyRating.average;
        }

        return EfficiencyRating.low;
    }

    /**
     * Builds expensive tools analysis.
     *
     * @param data tools data
     * @param companyAvg company avg cpu
     * @return analysis
     */
    private ExpensiveToolsAnalysisResponse buildExpensiveToolsAnalysis(
            List<ExpensiveToolResponse> data,
            BigDecimal companyAvg
    ) {

        BigDecimal savings =
                BigDecimal.ZERO;

        for (ExpensiveToolResponse tool : data) {

            if (tool.getEfficiencyRating()
                    == EfficiencyRating.low) {

                savings =
                        savings.add(
                                tool.getMonthlyCost()
                        );
            }
        }

        return new ExpensiveToolsAnalysisResponse(
                data.size(),
                companyAvg,
                numericService.roundMoney(
                        savings
                )
        );
    }

    /**
     * Builds empty expensive
     * tools analytics response.
     *
     * @return empty response
     */
    private ExpensiveToolsResponse buildEmptyExpensiveToolsResponse() {

        return new ExpensiveToolsResponse(
                Collections.emptyList(),
                "No analytics data available - ensure tools data exists",
                new ExpensiveToolsAnalysisResponse(
                        0,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO
                )
        );
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
                    "Invalid analytics parameter",
                    Map.of(
                            "sort_by",
                            "Allowed values: department, total_cost"
                    )
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
                    "Invalid analytics parameter",
                    Map.of(
                            "order",
                            "Allowed values: asc, desc"
                    )
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