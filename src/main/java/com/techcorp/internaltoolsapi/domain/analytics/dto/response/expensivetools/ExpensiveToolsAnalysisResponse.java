package com.techcorp.internaltoolsapi.domain.analytics.dto.response.expensivetools;

import java.math.BigDecimal;

/**
 * Company-level expensive tools analysis response.
 * <p>
 * Represents global analytics
 * for expensive tools.
 */
public class ExpensiveToolsAnalysisResponse {

    // ---------- ATTRIBUTES ---------- //

    private Integer totalToolsAnalyzed;

    private BigDecimal avgCostPerUserCompany;

    private BigDecimal potentialSavingsIdentified;

    // ---------- CONSTRUCTORS ---------- //

    public ExpensiveToolsAnalysisResponse() {
    }

    public ExpensiveToolsAnalysisResponse(
            Integer totalToolsAnalyzed,
            BigDecimal avgCostPerUserCompany,
            BigDecimal potentialSavingsIdentified
    ) {

        this.totalToolsAnalyzed =
                totalToolsAnalyzed;

        this.avgCostPerUserCompany =
                avgCostPerUserCompany;

        this.potentialSavingsIdentified =
                potentialSavingsIdentified;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public Integer getTotalToolsAnalyzed() {
        return totalToolsAnalyzed;
    }

    public void setTotalToolsAnalyzed(
            Integer totalToolsAnalyzed
    ) {
        this.totalToolsAnalyzed =
                totalToolsAnalyzed;
    }

    public BigDecimal getAvgCostPerUserCompany() {
        return avgCostPerUserCompany;
    }

    public void setAvgCostPerUserCompany(
            BigDecimal avgCostPerUserCompany
    ) {
        this.avgCostPerUserCompany =
                avgCostPerUserCompany;
    }

    public BigDecimal getPotentialSavingsIdentified() {
        return potentialSavingsIdentified;
    }

    public void setPotentialSavingsIdentified(
            BigDecimal potentialSavingsIdentified
    ) {
        this.potentialSavingsIdentified =
                potentialSavingsIdentified;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExpensiveToolsAnalysisResponse{");
        sb.append("totalToolsAnalyzed=").append(totalToolsAnalyzed);
        sb.append(", avgCostPerUserCompany=").append(avgCostPerUserCompany);
        sb.append(", potentialSavingsIdentified=").append(potentialSavingsIdentified);
        sb.append('}');
        return sb.toString();
    }
}