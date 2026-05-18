package com.techcorp.internaltoolsapi.analytics.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Usage analytics metrics for a specific period.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsagePeriodMetricsResponse {

    // ---------- ATTRIBUTES ---------- //

    private Integer totalSessions;

    private Integer avgSessionMinutes;

    // ---------- CONSTRUCTORS ---------- //

    public UsagePeriodMetricsResponse() {
    }

    public UsagePeriodMetricsResponse(
            Integer totalSessions,
            Integer avgSessionMinutes
    ) {

        this.totalSessions = totalSessions;
        this.avgSessionMinutes = avgSessionMinutes;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public Integer getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(Integer totalSessions) {
        this.totalSessions = totalSessions;
    }

    public Integer getAvgSessionMinutes() {
        return avgSessionMinutes;
    }

    public void setAvgSessionMinutes(Integer avgSessionMinutes) {
        this.avgSessionMinutes = avgSessionMinutes;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UsagePeriodMetricsResponse{");
        sb.append("totalSessions=").append(totalSessions);
        sb.append(", avgSessionMinutes=").append(avgSessionMinutes);
        sb.append('}');
        return sb.toString();
    }
}