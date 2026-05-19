package com.techcorp.internaltoolsapi.domain.analytics.service.impl;

import com.techcorp.internaltoolsapi.domain.analytics.dto.response.UsageMetricsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.UsagePeriodMetricsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.repository.UsageLogRepository;
import com.techcorp.internaltoolsapi.domain.analytics.service.AnalyticsService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Default analytics service implementation.
 */
@Service
public class AnalyticsServiceImpl
        implements AnalyticsService {

    // ---------- DEPENDENCIES ---------- //

    private final UsageLogRepository usageLogRepository;

    // ---------- CONSTRUCTOR ---------- //

    public AnalyticsServiceImpl(
            UsageLogRepository usageLogRepository
    ) {

        this.usageLogRepository =
                usageLogRepository;
    }

    // ---------- PUBLIC METHODS ---------- //

    /**
     * {@inheritDoc}
     */
    @Override
    public UsageMetricsResponse getUsageMetrics(
            Integer toolId
    ) {

        Object[] result =
                usageLogRepository.getUsageMetrics(
                        toolId,
                        LocalDate.now().minusDays(30)
                );

        Object[] metrics =
                (Object[]) result[0];

        long totalSessions =
                metrics[0] != null
                        ? ((Number) metrics[0]).longValue()
                        : 0L;

        double avgSessionMinutes =
                metrics[1] != null
                        ? ((Number) metrics[1]).doubleValue()
                        : 0.0;

        UsagePeriodMetricsResponse last30Days =
                new UsagePeriodMetricsResponse(
                        (int) totalSessions,
                        (int) Math.round(
                                avgSessionMinutes
                        )
                );

        return new UsageMetricsResponse(
                last30Days
        );
    }
}