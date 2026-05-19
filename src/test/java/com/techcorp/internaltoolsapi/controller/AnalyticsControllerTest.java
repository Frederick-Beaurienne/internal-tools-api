package com.techcorp.internaltoolsapi.controller;

import com.techcorp.internaltoolsapi.api.controller.AnalyticsController;
import com.techcorp.internaltoolsapi.api.exception.InvalidAnalyticsParameterException;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostSummaryResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostsResponse;
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
                            "Invalid sort_by parameter. Allowed values: department, total_cost."
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
                            .value("Invalid analytics parameter"))

                    .andExpect(jsonPath("$.message")
                            .value(
                                    "Invalid sort_by parameter. Allowed values: department, total_cost."
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
}