package com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost;

import java.math.BigDecimal;

/**
 * Company-level summary for
 * department cost analytics.
 * <p>
 * Provides global financial
 * and organizational insights.
 */
public class DepartmentCostSummaryResponse {

    // ---------- ATTRIBUTES ---------- //

    private BigDecimal totalCompanyCost;

    private Integer departmentsCount;

    private String mostExpensiveDepartment;

    // ---------- CONSTRUCTORS ---------- //

    public DepartmentCostSummaryResponse() {
    }

    public DepartmentCostSummaryResponse(
            BigDecimal totalCompanyCost,
            Integer departmentsCount,
            String mostExpensiveDepartment
    ) {

        this.totalCompanyCost =
                totalCompanyCost;

        this.departmentsCount =
                departmentsCount;

        this.mostExpensiveDepartment =
                mostExpensiveDepartment;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public BigDecimal getTotalCompanyCost() {
        return totalCompanyCost;
    }

    public void setTotalCompanyCost(
            BigDecimal totalCompanyCost
    ) {
        this.totalCompanyCost = totalCompanyCost;
    }

    public Integer getDepartmentsCount() {
        return departmentsCount;
    }

    public void setDepartmentsCount(
            Integer departmentsCount
    ) {
        this.departmentsCount = departmentsCount;
    }

    public String getMostExpensiveDepartment() {
        return mostExpensiveDepartment;
    }

    public void setMostExpensiveDepartment(
            String mostExpensiveDepartment
    ) {
        this.mostExpensiveDepartment =
                mostExpensiveDepartment;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DepartmentCostSummaryResponse{");
        sb.append("totalCompanyCost=").append(totalCompanyCost);
        sb.append(", departmentsCount=").append(departmentsCount);
        sb.append(", mostExpensiveDepartment='").append(mostExpensiveDepartment).append('\'');
        sb.append('}');
        return sb.toString();
    }
}