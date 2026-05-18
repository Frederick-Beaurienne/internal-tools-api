package com.techcorp.internaltoolsapi.tools.entity.enums;

/**
 * Enum representing tool owner departments.
 *
 * Values intentionally match PostgreSQL enum values exactly
 * to ensure clean native enum persistence and API consistency.
 */
public enum DepartmentType {

    Engineering,
    Sales,
    Marketing,
    HR,
    Finance,
    Operations,
    Design
}