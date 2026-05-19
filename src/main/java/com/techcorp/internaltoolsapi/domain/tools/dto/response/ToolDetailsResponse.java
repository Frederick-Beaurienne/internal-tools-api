package com.techcorp.internaltoolsapi.domain.tools.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.UsageMetricsResponse;

import java.math.BigDecimal;

/**
 * Detailed tool response including
 * analytics and financial metrics.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "description",
        "vendor",
        "category",
        "monthly_cost",
        "owner_department",
        "status",
        "website_url",
        "active_users_count",
        "total_monthly_cost",
        "created_at",
        "updated_at",
        "usage_metrics"
})
public class ToolDetailsResponse
        extends ToolResponse {

    // ---------- ATTRIBUTES ---------- //

    private BigDecimal totalMonthlyCost;

    private UsageMetricsResponse usageMetrics;

    // ---------- CONSTRUCTORS ---------- //

    public ToolDetailsResponse() {
    }

    // ---------- GETTERS & SETTERS ---------- //

    public BigDecimal getTotalMonthlyCost() {
        return totalMonthlyCost;
    }

    public void setTotalMonthlyCost(
            BigDecimal totalMonthlyCost
    ) {
        this.totalMonthlyCost = totalMonthlyCost;
    }

    public UsageMetricsResponse getUsageMetrics() {
        return usageMetrics;
    }

    public void setUsageMetrics(
            UsageMetricsResponse usageMetrics
    ) {
        this.usageMetrics = usageMetrics;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {

        final StringBuilder sb =
                new StringBuilder(
                        "ToolDetailsResponse{"
                );

        sb.append("totalMonthlyCost=")
                .append(totalMonthlyCost);

        sb.append(", usageMetrics=")
                .append(usageMetrics);

        sb.append('}');

        return sb.toString();
    }
}