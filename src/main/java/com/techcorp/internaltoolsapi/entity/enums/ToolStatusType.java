package com.techcorp.internaltoolsapi.entity.enums;

/**
 * Enum representing tool lifecycle status.
 *
 * Values intentionally match PostgreSQL enum values exactly
 * to ensure clean native enum persistence and API consistency.
 */
public enum ToolStatusType {

    active,
    deprecated,
    trial
}