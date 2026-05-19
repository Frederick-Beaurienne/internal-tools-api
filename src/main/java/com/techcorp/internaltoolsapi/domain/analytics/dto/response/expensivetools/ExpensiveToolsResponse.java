package com.techcorp.internaltoolsapi.domain.analytics.dto.response.expensivetools;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Expensive tools analytics response.
 * <p>
 * Represents expensive tools
 * analytics payload.
 */
@JsonInclude(
        JsonInclude.Include.NON_NULL
)
public class ExpensiveToolsResponse {

    // ---------- ATTRIBUTES ---------- //

    private List<ExpensiveToolResponse> data;

    private String message;

    private ExpensiveToolsAnalysisResponse analysis;

    // ---------- CONSTRUCTORS ---------- //

    public ExpensiveToolsResponse() {
    }

    public ExpensiveToolsResponse(
            List<ExpensiveToolResponse> data,
            ExpensiveToolsAnalysisResponse analysis
    ) {

        this.data = data;
        this.analysis = analysis;
    }

    public ExpensiveToolsResponse(
            List<ExpensiveToolResponse> data,
            String message,
            ExpensiveToolsAnalysisResponse analysis
    ) {

        this.data = data;
        this.message = message;
        this.analysis = analysis;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public List<ExpensiveToolResponse> getData() {
        return data;
    }

    public void setData(
            List<ExpensiveToolResponse> data
    ) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(
            String message
    ) {
        this.message = message;
    }

    public ExpensiveToolsAnalysisResponse getAnalysis() {
        return analysis;
    }

    public void setAnalysis(
            ExpensiveToolsAnalysisResponse analysis
    ) {
        this.analysis = analysis;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExpensiveToolsResponse{");
        sb.append("data=").append(data);
        sb.append(", message='").append(message).append('\'');
        sb.append(", analysis=").append(analysis);
        sb.append('}');
        return sb.toString();
    }
}