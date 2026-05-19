package com.techcorp.internaltoolsapi.controller;

import com.techcorp.internaltoolsapi.api.controller.AnalyticsController;
import com.techcorp.internaltoolsapi.api.exception.InvalidAnalyticsParameterException;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostSummaryResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.expensivetools.ExpensiveToolResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.expensivetools.ExpensiveToolsAnalysisResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.expensivetools.ExpensiveToolsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.enums.EfficiencyRating;
import com.techcorp.internaltoolsapi.domain.analytics.service.AnalyticsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller tests for AnalyticsController endpoints.
 */
@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    // ---------- DEPENDENCIES ---------- //

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnalyticsService analyticsService;

    // ---------- TEST DATA ---------- //

    private DepartmentCostsResponse createMockAnalyticsResponse() {

        return new DepartmentCostsResponse(
                List.of(
                        new DepartmentCostResponse(
                                "Engineering",
                                new BigDecimal("2495.50"),
                                7,
                                329,
                                new BigDecimal("356.50"),
                                81.0
                        )
                ),
                new DepartmentCostSummaryResponse(
                        new BigDecimal("3081.64"),
                        7,
                        "Engineering"
                )
        );
    }

    private DepartmentCostsResponse createEmptyAnalyticsResponse() {

        return new DepartmentCostsResponse(
                Collections.emptyList(),
                "No analytics data available - ensure tools data exists",
                new DepartmentCostSummaryResponse(
                        BigDecimal.ZERO,
                        0,
                        null
                )
        );
    }

    // ---------- TEST GROUPS ---------- //

    @Nested
    @DisplayName("GET endpoints tests")
    class GetEndpointsTests {

        @Test
        @DisplayName("Should return default department analytics")
        void shouldReturnDefaultDepartmentAnalytics()
                throws Exception {

            when(analyticsService.getDepartmentCosts(
                    null,
                    null
            )).thenReturn(
                    createMockAnalyticsResponse()
            );

            mockMvc.perform(
                            get("/api/analytics/department-costs")
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath("$.data.length()")
                            .value(1))

                    .andExpect(jsonPath("$.data[0].department")
                            .value("Engineering"))

                    .andExpect(jsonPath("$.data[0].total_cost")
                            .value(2495.50))

                    .andExpect(jsonPath("$.summary.total_company_cost")
                            .value(3081.64))

                    .andExpect(jsonPath("$.summary.departments_count")
                            .value(7))

                    .andExpect(jsonPath(
                            "$.summary.most_expensive_department"
                    ).value("Engineering"));

            verify(analyticsService)
                    .getDepartmentCosts(
                            null,
                            null
                    );
        }

        @Test
        @DisplayName("Should sort department analytics by total cost")
        void shouldSortDepartmentAnalyticsByTotalCost()
                throws Exception {

            when(analyticsService.getDepartmentCosts(
                    "total_cost",
                    "desc"
            )).thenReturn(
                    createMockAnalyticsResponse()
            );

            mockMvc.perform(
                            get("/api/analytics/department-costs")
                                    .param(
                                            "sort_by",
                                            "total_cost"
                                    )
                                    .param(
                                            "order",
                                            "desc"
                                    )
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath(
                            "$.data[0].department"
                    ).value("Engineering"));

            verify(analyticsService)
                    .getDepartmentCosts(
                            "total_cost",
                            "desc"
                    );
        }

        @Test
        @DisplayName("Should return 400 for invalid sort_by parameter")
        void shouldReturn400ForInvalidSortByParameter()
                throws Exception {

            when(analyticsService.getDepartmentCosts(
                    "invalid",
                    null
            )).thenThrow(
                    new InvalidAnalyticsParameterException(
                            "Invalid sort_by parameter",
                            Map.of(
                                    "sort_by",
                                    "Allowed values: department, total_cost"
                            )
                    )
            );

            mockMvc.perform(
                            get("/api/analytics/department-costs")
                                    .param(
                                            "sort_by",
                                            "invalid"
                                    )
                    )

                    .andExpect(status().isBadRequest())

                    .andExpect(jsonPath("$.error")
                            .value("Validation failed"))

                    .andExpect(jsonPath("$.message")
                            .value("Invalid analytics parameter"))

                    .andExpect(jsonPath("$.details.sort_by")
                            .value(
                                    "Allowed values: department, total_cost"
                            ))

                    .andExpect(jsonPath("$.timestamp")
                            .exists());
        }

        @Test
        @DisplayName("Should return empty analytics response when no active tools exist")
        void shouldReturnEmptyAnalyticsResponseWhenNoActiveToolsExist()
                throws Exception {

            when(analyticsService.getDepartmentCosts(
                    null,
                    null
            )).thenReturn(
                    createEmptyAnalyticsResponse()
            );

            mockMvc.perform(
                            get("/api/analytics/department-costs")
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath("$.data.length()")
                            .value(0))

                    .andExpect(jsonPath("$.message")
                            .value(
                                    "No analytics data available - ensure tools data exists"
                            ))

                    .andExpect(jsonPath("$.summary.total_company_cost")
                            .value(0));

            verify(analyticsService)
                    .getDepartmentCosts(
                            null,
                            null
                    );
        }
    }

    @Nested
    @DisplayName("Expensive tools endpoints tests")
    class ExpensiveToolsEndpointsTests {

        private ExpensiveToolsResponse createMockExpensiveToolsResponse() {

            return new ExpensiveToolsResponse(
                    List.of(
                            new ExpensiveToolResponse(
                                    1,
                                    "Slack Enterprise",
                                    new BigDecimal("1800.00"),
                                    220,
                                    new BigDecimal("8.18"),
                                    "Engineering",
                                    "Slack",
                                    EfficiencyRating.low
                            )
                    ),
                    new ExpensiveToolsAnalysisResponse(
                            1,
                            new BigDecimal("5.97"),
                            new BigDecimal("1800.00")
                    )
            );
        }

        @Test
        @DisplayName("Should return default expensive tools analytics")
        void shouldReturnDefaultExpensiveToolsAnalytics()
                throws Exception {

            when(analyticsService.getExpensiveTools(
                    null,
                    null
            )).thenReturn(
                    createMockExpensiveToolsResponse()
            );

            mockMvc.perform(
                            get("/api/analytics/expensive-tools")
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath("$.data.length()")
                            .value(1))

                    .andExpect(jsonPath("$.data[0].name")
                            .value("Slack Enterprise"))

                    .andExpect(jsonPath("$.data[0].monthly_cost")
                            .value(1800.00))

                    .andExpect(jsonPath("$.analysis.total_tools_analyzed")
                            .value(1))

                    .andExpect(jsonPath("$.analysis.avg_cost_per_user_company")
                            .value(5.97))

                    .andExpect(jsonPath("$.analysis.potential_savings_identified")
                            .value(1800.00));

            verify(analyticsService)
                    .getExpensiveTools(
                            null,
                            null
                    );
        }

        @Test
        @DisplayName("Should filter expensive tools by min cost and limit")
        void shouldFilterExpensiveToolsByMinCostAndLimit()
                throws Exception {

            when(analyticsService.getExpensiveTools(
                    new BigDecimal("50"),
                    5
            )).thenReturn(
                    createMockExpensiveToolsResponse()
            );

            mockMvc.perform(
                            get("/api/analytics/expensive-tools")
                                    .param(
                                            "min_cost",
                                            "50"
                                    )
                                    .param(
                                            "limit",
                                            "5"
                                    )
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath("$.data[0].name")
                            .value("Slack Enterprise"));

            verify(analyticsService)
                    .getExpensiveTools(
                            new BigDecimal("50"),
                            5
                    );
        }

        @Test
        @DisplayName("Should return 400 for invalid limit parameter")
        void shouldReturn400ForInvalidLimitParameter()
                throws Exception {

            when(analyticsService.getExpensiveTools(
                    null,
                    -5
            )).thenThrow(
                    new InvalidAnalyticsParameterException(
                            "Invalid analytics parameter",
                            Map.of(
                                    "limit",
                                    "Must be positive integer between 1 and 100"
                            )
                    )
            );

            mockMvc.perform(
                            get("/api/analytics/expensive-tools")
                                    .param(
                                            "limit",
                                            "-5"
                                    )
                    )

                    .andExpect(status().isBadRequest())

                    .andExpect(jsonPath("$.error")
                            .value("Validation failed"))

                    .andExpect(jsonPath("$.message")
                            .value("Invalid analytics parameter"))

                    .andExpect(jsonPath("$.details.limit")
                            .value(
                                    "Must be positive integer between 1 and 100"
                            ))

                    .andExpect(jsonPath("$.timestamp")
                            .exists());
        }
    }
}