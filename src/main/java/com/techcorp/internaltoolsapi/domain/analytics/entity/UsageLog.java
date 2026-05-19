package com.techcorp.internaltoolsapi.domain.analytics.entity;

import com.techcorp.internaltoolsapi.domain.tools.entity.Tool;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Usage tracking entry for tool activity analytics.
 */
@Entity
@Table(name = "usage_logs")
public class UsageLog {

    // ---------- ATTRIBUTES ---------- //

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "tool_id",
            nullable = false
    )
    private Tool tool;

    @Column(name = "user_id")
    private Integer userId;

    @Column(
            name = "session_date",
            nullable = false
    )
    private LocalDate sessionDate;

    @Column(name = "usage_minutes")
    private Integer usageMinutes;

    @Column(name = "actions_count")
    private Integer actionsCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ---------- CONSTRUCTORS ---------- //

    public UsageLog() {
    }

    // ---------- GETTERS & SETTERS ---------- //

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(
            LocalDate sessionDate
    ) {
        this.sessionDate = sessionDate;
    }

    public Integer getUsageMinutes() {
        return usageMinutes;
    }

    public void setUsageMinutes(
            Integer usageMinutes
    ) {
        this.usageMinutes = usageMinutes;
    }

    public Integer getActionsCount() {
        return actionsCount;
    }

    public void setActionsCount(
            Integer actionsCount
    ) {
        this.actionsCount = actionsCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }

    // ---------- TO STRING ---------- //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UsageLog{");
        sb.append("id=").append(id);
        sb.append(", tool=").append(tool);
        sb.append(", userId=").append(userId);
        sb.append(", sessionDate=").append(sessionDate);
        sb.append(", usageMinutes=").append(usageMinutes);
        sb.append(", actionsCount=").append(actionsCount);
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}