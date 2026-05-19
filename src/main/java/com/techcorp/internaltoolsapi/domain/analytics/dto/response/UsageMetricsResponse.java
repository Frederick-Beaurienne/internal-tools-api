package com.techcorp.internaltoolsapi.domain.analytics.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Aggregated usage analytics metrics.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsageMetricsResponse {

    // ---------- ATTRIBUTES ---------- //

    @JsonProperty("last_30_days")
    private UsagePeriodMetricsResponse last30Days;

    // ---------- CONSTRUCTORS ---------- //

    public UsageMetricsResponse() {
    }

    public UsageMetricsResponse(
            UsagePeriodMetricsResponse last30Days
    ) {

        this.last30Days = last30Days;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public UsagePeriodMetricsResponse getLast30Days() {
        return last30Days;
    }

    public void setLast30Days(
            UsagePeriodMetricsResponse last30Days
    ) {
        this.last30Days = last30Days;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {

        final StringBuilder sb =
                new StringBuilder("UsageMetricsResponse{");

        sb.append("last30Days=")
                .append(last30Days);

        sb.append('}');

        return sb.toString();
    }
}