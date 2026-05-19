package com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost;

import java.math.BigDecimal;

/**
 * Department-level cost analytics response.
 * <p>
 * Represents aggregated analytics
 * for a single department.
 */
public class DepartmentCostResponse {

    // ---------- ATTRIBUTES ---------- //

    private String department;

    private BigDecimal totalCost;

    private Integer toolsCount;

    private Integer totalUsers;

    private BigDecimal averageCostPerTool;

    private Double costPercentage;

    // ---------- CONSTRUCTORS ---------- //

    public DepartmentCostResponse() {
    }

    public DepartmentCostResponse(
            String department,
            BigDecimal totalCost,
            Integer toolsCount,
            Integer totalUsers,
            BigDecimal averageCostPerTool,
            Double costPercentage
    ) {

        this.department = department;
        this.totalCost = totalCost;
        this.toolsCount = toolsCount;
        this.totalUsers = totalUsers;
        this.averageCostPerTool = averageCostPerTool;
        this.costPercentage = costPercentage;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public String getDepartment() {
        return department;
    }

    public void setDepartment(
            String department
    ) {
        this.department = department;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(
            BigDecimal totalCost
    ) {
        this.totalCost = totalCost;
    }

    public Integer getToolsCount() {
        return toolsCount;
    }

    public void setToolsCount(
            Integer toolsCount
    ) {
        this.toolsCount = toolsCount;
    }

    public Integer getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(
            Integer totalUsers
    ) {
        this.totalUsers = totalUsers;
    }

    public BigDecimal getAverageCostPerTool() {
        return averageCostPerTool;
    }

    public void setAverageCostPerTool(
            BigDecimal averageCostPerTool
    ) {
        this.averageCostPerTool = averageCostPerTool;
    }

    public Double getCostPercentage() {
        return costPercentage;
    }

    public void setCostPercentage(
            Double costPercentage
    ) {
        this.costPercentage = costPercentage;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DepartmentCostResponse{");
        sb.append("department='").append(department).append('\'');
        sb.append(", totalCost=").append(totalCost);
        sb.append(", toolsCount=").append(toolsCount);
        sb.append(", totalUsers=").append(totalUsers);
        sb.append(", averageCostPerTool=").append(averageCostPerTool);
        sb.append(", costPercentage=").append(costPercentage);
        sb.append('}');
        return sb.toString();
    }
}