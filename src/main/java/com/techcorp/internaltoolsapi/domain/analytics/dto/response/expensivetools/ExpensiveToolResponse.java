package com.techcorp.internaltoolsapi.domain.analytics.dto.response.expensivetools;

import com.techcorp.internaltoolsapi.domain.analytics.enums.EfficiencyRating;

import java.math.BigDecimal;

/**
 * Tool-level expensive analytics response.
 * <p>
 * Represents analytics
 * for a single tool.
 */
public class ExpensiveToolResponse {

    // ---------- ATTRIBUTES ---------- //

    private Integer id;

    private String name;

    private BigDecimal monthlyCost;

    private Integer activeUsersCount;

    private BigDecimal costPerUser;

    private String department;

    private String vendor;

    private EfficiencyRating efficiencyRating;

    // ---------- CONSTRUCTORS ---------- //

    public ExpensiveToolResponse() {
    }

    public ExpensiveToolResponse(
            Integer id,
            String name,
            BigDecimal monthlyCost,
            Integer activeUsersCount,
            BigDecimal costPerUser,
            String department,
            String vendor,
            EfficiencyRating efficiencyRating
    ) {

        this.id = id;
        this.name = name;
        this.monthlyCost = monthlyCost;
        this.activeUsersCount = activeUsersCount;
        this.costPerUser = costPerUser;
        this.department = department;
        this.vendor = vendor;
        this.efficiencyRating = efficiencyRating;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public Integer getId() {
        return id;
    }

    public void setId(
            Integer id
    ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    public BigDecimal getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(
            BigDecimal monthlyCost
    ) {
        this.monthlyCost = monthlyCost;
    }

    public Integer getActiveUsersCount() {
        return activeUsersCount;
    }

    public void setActiveUsersCount(
            Integer activeUsersCount
    ) {
        this.activeUsersCount = activeUsersCount;
    }

    public BigDecimal getCostPerUser() {
        return costPerUser;
    }

    public void setCostPerUser(
            BigDecimal costPerUser
    ) {
        this.costPerUser = costPerUser;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(
            String department
    ) {
        this.department = department;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(
            String vendor
    ) {
        this.vendor = vendor;
    }

    public EfficiencyRating getEfficiencyRating() {
        return efficiencyRating;
    }

    public void setEfficiencyRating(
            EfficiencyRating efficiencyRating
    ) {
        this.efficiencyRating = efficiencyRating;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExpensiveToolResponse{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", monthlyCost=").append(monthlyCost);
        sb.append(", activeUsersCount=").append(activeUsersCount);
        sb.append(", costPerUser=").append(costPerUser);
        sb.append(", department='").append(department).append('\'');
        sb.append(", vendor='").append(vendor).append('\'');
        sb.append(", efficiencyRating=").append(efficiencyRating);
        sb.append('}');
        return sb.toString();
    }
}