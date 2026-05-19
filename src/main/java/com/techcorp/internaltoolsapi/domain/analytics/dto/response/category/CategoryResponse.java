package com.techcorp.internaltoolsapi.domain.analytics.dto.response.category;

import java.math.BigDecimal;

/**
 * Category-level analytics response.
 * <p>
 * Represents aggregated analytics
 * for a single category.
 */
public class CategoryResponse {

    // ---------- ATTRIBUTES ---------- //

    private String categoryName;

    private Integer toolsCount;

    private BigDecimal totalCost;

    private Integer totalUsers;

    private Double percentageOfBudget;

    private BigDecimal averageCostPerUser;

    // ---------- CONSTRUCTORS ---------- //

    public CategoryResponse() {
    }

    public CategoryResponse(
            String categoryName,
            Integer toolsCount,
            BigDecimal totalCost,
            Integer totalUsers,
            Double percentageOfBudget,
            BigDecimal averageCostPerUser
    ) {

        this.categoryName = categoryName;
        this.toolsCount = toolsCount;
        this.totalCost = totalCost;
        this.totalUsers = totalUsers;
        this.percentageOfBudget = percentageOfBudget;
        this.averageCostPerUser = averageCostPerUser;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(
            String categoryName
    ) {
        this.categoryName = categoryName;
    }

    public Integer getToolsCount() {
        return toolsCount;
    }

    public void setToolsCount(
            Integer toolsCount
    ) {
        this.toolsCount = toolsCount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(
            BigDecimal totalCost
    ) {
        this.totalCost = totalCost;
    }

    public Integer getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(
            Integer totalUsers
    ) {
        this.totalUsers = totalUsers;
    }

    public Double getPercentageOfBudget() {
        return percentageOfBudget;
    }

    public void setPercentageOfBudget(
            Double percentageOfBudget
    ) {
        this.percentageOfBudget = percentageOfBudget;
    }

    public BigDecimal getAverageCostPerUser() {
        return averageCostPerUser;
    }

    public void setAverageCostPerUser(
            BigDecimal averageCostPerUser
    ) {
        this.averageCostPerUser = averageCostPerUser;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CategoryResponse{");
        sb.append("categoryName='").append(categoryName).append('\'');
        sb.append(", toolsCount=").append(toolsCount);
        sb.append(", totalCost=").append(totalCost);
        sb.append(", totalUsers=").append(totalUsers);
        sb.append(", percentageOfBudget=").append(percentageOfBudget);
        sb.append(", averageCostPerUser=").append(averageCostPerUser);
        sb.append('}');
        return sb.toString();
    }
}