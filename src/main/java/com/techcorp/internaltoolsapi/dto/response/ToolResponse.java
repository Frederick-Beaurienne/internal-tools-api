package com.techcorp.internaltoolsapi.dto.response;

import com.techcorp.internaltoolsapi.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.entity.enums.ToolStatusType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Response DTO representing a tool.
 */
public class ToolResponse {

    // ---------- ATTRIBUTES ---------- //

    private Integer id;

    private String name;

    private String description;

    private String vendor;

    private String websiteUrl;

    private String category;

    private BigDecimal monthlyCost;

    private DepartmentType ownerDepartment;

    private ToolStatusType status;

    private Integer activeUsersCount;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    // ---------- CONSTRUCTORS ---------- //

    public ToolResponse() {
    }

    public ToolResponse(Integer id,
                        String name,
                        String description,
                        String vendor,
                        String websiteUrl,
                        String category,
                        BigDecimal monthlyCost,
                        DepartmentType ownerDepartment,
                        ToolStatusType status,
                        Integer activeUsersCount,
                        OffsetDateTime createdAt,
                        OffsetDateTime updatedAt) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.vendor = vendor;
        this.websiteUrl = websiteUrl;
        this.category = category;
        this.monthlyCost = monthlyCost;
        this.ownerDepartment = ownerDepartment;
        this.status = status;
        this.activeUsersCount = activeUsersCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ---------- GETTERS & SETTERS ---------- //

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getMonthlyCost() {
        return monthlyCost;
    }

    public DepartmentType getOwnerDepartment() {
        return ownerDepartment;
    }

    public ToolStatusType getStatus() {
        return status;
    }

    public Integer getActiveUsersCount() {
        return activeUsersCount;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(
            Integer id
    ) {
        this.id = id;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    public void setDescription(
            String description
    ) {
        this.description = description;
    }

    public void setVendor(
            String vendor
    ) {
        this.vendor = vendor;
    }

    public void setWebsiteUrl(
            String websiteUrl
    ) {
        this.websiteUrl = websiteUrl;
    }

    public void setCategory(
            String category
    ) {
        this.category = category;
    }

    public void setMonthlyCost(
            BigDecimal monthlyCost
    ) {
        this.monthlyCost = monthlyCost;
    }

    public void setOwnerDepartment(
            DepartmentType ownerDepartment
    ) {
        this.ownerDepartment = ownerDepartment;
    }

    public void setStatus(
            ToolStatusType status
    ) {
        this.status = status;
    }

    public void setActiveUsersCount(
            Integer activeUsersCount
    ) {
        this.activeUsersCount = activeUsersCount;
    }

    public void setCreatedAt(
            OffsetDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(
            OffsetDateTime updatedAt
    ) {
        this.updatedAt = updatedAt;
    }

    // ---------- OBJECT METHODS ---------- //

    @Override
    public String toString() {

        final StringBuilder sb =
                new StringBuilder("ToolResponse{");

        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", vendor='").append(vendor).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", monthlyCost=").append(monthlyCost);
        sb.append(", status=").append(status);
        sb.append('}');

        return sb.toString();
    }
}