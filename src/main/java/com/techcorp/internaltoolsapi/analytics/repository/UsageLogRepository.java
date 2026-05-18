package com.techcorp.internaltoolsapi.analytics.repository;

import com.techcorp.internaltoolsapi.analytics.entity.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Repository for usage analytics queries.
 */
@Repository
public interface UsageLogRepository
        extends JpaRepository<UsageLog, Integer> {

    /**
     * Retrieves aggregated usage metrics
     * for a tool since a given date.
     *
     * @param toolId tool identifier
     * @param since lower date bound
     * @return aggregation result:
     *         [0] = total sessions
     *         [1] = average session minutes
     */
    @Query("""
        SELECT
            COUNT(u),
            COALESCE(AVG(u.usageMinutes), 0)
        FROM UsageLog u
        WHERE u.tool.id = :toolId
          AND u.sessionDate >= :since
    """)
    Object[] getUsageMetrics(
            Integer toolId,
            LocalDate since
    );
}