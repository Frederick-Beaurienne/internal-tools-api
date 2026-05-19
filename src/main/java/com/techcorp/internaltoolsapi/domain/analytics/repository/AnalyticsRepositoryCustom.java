package com.techcorp.internaltoolsapi.domain.analytics.repository;

import com.techcorp.internaltoolsapi.domain.analytics.entity.enums.DepartmentCostSortField;
import com.techcorp.internaltoolsapi.domain.tools.entity.enums.ToolStatusType;
import com.techcorp.internaltoolsapi.shared.sort.SortDirection;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface AnalyticsRepositoryCustom {

    List<Object[]> getDepartmentCosts(
            ToolStatusType status,
            DepartmentCostSortField sortField,
            SortDirection direction
    );

    /**
     * Retrieves expensive tools
     * ordered by monthly cost.
     *
     * @param minCost  minimum monthly cost
     * @param pageable result limit
     * @return expensive tools
     */
    List<Object[]> findExpensiveTools(
            BigDecimal minCost,
            Pageable pageable
    );

    /**
     * Retrieves all active tools
     * for company-wide
     * cost per user analytics.
     *
     * @return active tools projection
     */
    List<Object[]> findActiveToolsForCompanyAverage();

    /**
     * Retrieves category analytics
     * aggregation for active tools.
     *
     * @return category analytics projection
     */
    List<Object[]> getToolsByCategory();
}