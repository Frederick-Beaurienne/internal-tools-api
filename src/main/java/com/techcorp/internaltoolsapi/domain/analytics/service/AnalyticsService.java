package com.techcorp.internaltoolsapi.domain.analytics.service;


import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.usage.UsageMetricsResponse;

/**
 * Service responsible for analytics,
 * reporting and cost optimization logic.
 */
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
}