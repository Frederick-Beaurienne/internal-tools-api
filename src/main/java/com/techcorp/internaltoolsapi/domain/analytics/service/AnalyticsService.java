package com.techcorp.internaltoolsapi.domain.analytics.service;


import com.techcorp.internaltoolsapi.domain.analytics.dto.response.category.CategoryToolsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.expensivetools.ExpensiveToolsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.usage.UsageMetricsResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service responsible for analytics,
 * reporting and cost optimization logic.
 */
@Service
public interface AnalyticsService {

    /**
     * Retrieves usage metrics for a tool
     * over the last 30 days.
     *
     * @param toolId tool identifier
     * @return usage metrics response
     */
    UsageMetricsResponse getUsageMetrics(
            Integer toolId
    );

    /**
     * Retrieves department cost analytics.
     * <p>
     * The analytics include:
     * - department cost aggregation
     * - tools count
     * - users count
     * - cost averages
     * - budget distribution percentages
     * - company-level summary insights
     * <p>
     * Only active tools are included
     * in analytics calculations.
     *
     * @param sortBy optional sorting field
     * @param order  optional sorting direction
     * @return department cost analytics response
     */
    DepartmentCostsResponse getDepartmentCosts(
            String sortBy,
            String order
    );

    /**
     * Retrieves expensive tools analytics.
     * <p>
     * Analytics include:
     * - expensive tools ranking
     * - cost per user
     * - efficiency rating
     * - company-level analysis
     *
     * @param minCost optional minimum monthly cost
     * @param limit maximum tools returned
     * @return expensive tools analytics
     */
    ExpensiveToolsResponse getExpensiveTools(
            BigDecimal minCost,
            Integer limit
    );

    /**
     * Retrieves category analytics.
     * <p>
     * Analytics include:
     * - category aggregation
     * - tools count
     * - users count
     * - budget percentages
     * - category insights
     * <p>
     * Only active tools are included.
     *
     * @return category analytics
     */
    CategoryToolsResponse getToolsByCategory();
}