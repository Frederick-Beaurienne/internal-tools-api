package com.techcorp.internaltoolsapi.analytics.service;


import com.techcorp.internaltoolsapi.analytics.dto.response.UsageMetricsResponse;

/**
 * Service exposing analytics-related operations.
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
}