package com.techcorp.internaltoolsapi.domain.analytics.repository;

import com.techcorp.internaltoolsapi.domain.tools.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository dedicated to analytics
 * and reporting persistence queries.
 */
public interface AnalyticsRepository
        extends JpaRepository<Tool, Integer>, AnalyticsRepositoryCustom {
}