package com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Department cost analytics response wrapper.
 * <p>
 * Provides:
 * - department analytics data
 * - company-level summary insights
 * <p>
 * A dedicated wrapper is intentionally used
 * in order to:
 * - provide a stable API contract
 * - separate analytics data from summary insights
 * - preserve response extensibility
 * - align with analytics endpoint requirements
 */
public class DepartmentCostsResponse {

    // ---------- ATTRIBUTES ---------- //

    private List<DepartmentCostResponse> data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    private DepartmentCostSummaryResponse summary;

    // ---------- CONSTRUCTORS ---------- //

    public DepartmentCostsResponse() {
    }

    public DepartmentCostsResponse(
            List<DepartmentCostResponse> data,
            DepartmentCostSummaryResponse summary
    ) {

        this.data = data;
        this.summary = summary;
    }

    public DepartmentCostsResponse(
            List<DepartmentCostResponse> data,
            String message,
            DepartmentCostSummaryResponse summary
    ) {

        this.data = data;
        this.message = message;
        this.summary = summary;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public List<DepartmentCostResponse> getData() {
        return data;
    }

    public void setData(
            List<DepartmentCostResponse> data
    ) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DepartmentCostSummaryResponse getSummary() {
        return summary;
    }

    public void setSummary(
            DepartmentCostSummaryResponse summary
    ) {
        this.summary = summary;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DepartmentCostsResponse{");
        sb.append("data=").append(data);
        sb.append(", message='").append(message).append('\'');
        sb.append(", summary=").append(summary);
        sb.append('}');
        return sb.toString();
    }
}