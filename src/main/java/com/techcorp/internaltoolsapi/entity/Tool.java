package com.techcorp.internaltoolsapi.entity;

import com.techcorp.internaltoolsapi.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.entity.enums.ToolStatusType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Tool entity representing internal SaaS tools.
 */
@Entity
@Table(name = "tools")
public class Tool {

    // ---------- ATTRIBUTES ---------- //

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,
            unique = true,
            length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false,
            length = 100)
    private String vendor;

    @Column(name = "website_url")
    private String websiteUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "monthly_cost",
            nullable = false)
    private BigDecimal monthlyCost;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "owner_department")
    private DepartmentType ownerDepartment;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status")
    private ToolStatusType status;

    @Column(name = "active_users_count")
    private Integer activeUsersCount;

    @Column(name = "created_at",
            updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    // ---------- CONSTRUCTORS ---------- //

    public Tool() {
    }

    public Tool(Integer id,
                String name,
                String description,
                String vendor,
                String websiteUrl,
                Category category,
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

    public Category getCategory() {
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
            Category category
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
        final StringBuilder sb = new StringBuilder("Tool{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", vendor='").append(vendor).append('\'');
        sb.append(", websiteUrl='").append(websiteUrl).append('\'');
        sb.append(", category=").append(category);
        sb.append(", monthlyCost=").append(monthlyCost);
        sb.append(", ownerDepartment=").append(ownerDepartment);
        sb.append(", status=").append(status);
        sb.append(", activeUsersCount=").append(activeUsersCount);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append('}');
        return sb.toString();
    }
}