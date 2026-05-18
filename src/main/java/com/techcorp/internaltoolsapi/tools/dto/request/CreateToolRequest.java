package com.techcorp.internaltoolsapi.tools.dto.request;

import com.techcorp.internaltoolsapi.tools.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.tools.entity.enums.ToolStatusType;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Request DTO used for tool creation.
 */
public class CreateToolRequest {

    // ---------- ATTRIBUTES ---------- //

    @Schema(
            description = "Tool name",
            example = "Slack"
    )
    @NotBlank(message = "Tool name is required")
    @Size(
            max = 100,
            message = "Tool name must not exceed 100 characters"
    )
    private String name;

    @Schema(
            description = "Tool description",
            example = "Team communication platform"
    )
    @Size(
            max = 1000,
            message = "Description must not exceed 1000 characters"
    )
    private String description;

    @Schema(
            description = "Tool vendor",
            example = "Slack"
    )
    @NotBlank(message = "Vendor is required")
    @Size(
            max = 100,
            message = "Vendor must not exceed 100 characters"
    )
    private String vendor;

    @Schema(
            description = "Tool website URL",
            example = "https://slack.com"
    )
    private String websiteUrl;

    @Schema(
            description = "Category ID",
            example = "1"
    )
    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @Schema(
            description = "Monthly subscription cost",
            example = "1200.00"
    )
    @NotNull(message = "Monthly cost is required")
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Monthly cost must be positive"
    )
    private BigDecimal monthlyCost;

    @Schema(
            description = "Owner department"
    )
    @NotNull(message = "Owner department is required")
    private DepartmentType ownerDepartment;

    @Schema(
            description = "Tool lifecycle status"
    )
    private ToolStatusType status;

    @Schema(
            description = "Active users count",
            example = "150"
    )
    @Min(
            value = 0,
            message = "Active users count must be positive"
    )
    private Integer activeUsersCount;

    // ---------- CONSTRUCTORS ---------- //

    public CreateToolRequest() {
    }

    // ---------- GETTERS & SETTERS ---------- //

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description
    ) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(
            String vendor
    ) {
        this.vendor = vendor;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(
            String websiteUrl
    ) {
        this.websiteUrl = websiteUrl;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(
            Integer categoryId
    ) {
        this.categoryId = categoryId;
    }

    public BigDecimal getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(
            BigDecimal monthlyCost
    ) {
        this.monthlyCost = monthlyCost;
    }

    public DepartmentType getOwnerDepartment() {
        return ownerDepartment;
    }

    public void setOwnerDepartment(
            DepartmentType ownerDepartment
    ) {
        this.ownerDepartment = ownerDepartment;
    }

    public ToolStatusType getStatus() {
        return status;
    }

    public void setStatus(
            ToolStatusType status
    ) {
        this.status = status;
    }

    public Integer getActiveUsersCount() {
        return activeUsersCount;
    }

    public void setActiveUsersCount(
            Integer activeUsersCount
    ) {
        this.activeUsersCount = activeUsersCount;
    }
}