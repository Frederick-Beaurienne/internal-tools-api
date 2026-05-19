package com.techcorp.internaltoolsapi.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Full integration tests
 * for analytics API workflow.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnalyticsIntegrationTest {

    // ---------- DEPENDENCIES ---------- //

    @Autowired
    private MockMvc mockMvc;

    // ---------- TEST GROUPS ---------- //

    @Nested
    @DisplayName("Department costs analytics tests")
    class DepartmentCostsAnalyticsTests {

        @Test
        @DisplayName("Should retrieve department costs from PostgreSQL")
        void shouldRetrieveDepartmentCostsFromPostgreSQL()
                throws Exception {

            mockMvc.perform(
                            get("/api/analytics/department-costs")
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath("$.summary")
                            .exists())

                    .andExpect(jsonPath("$.summary.total_company_cost")
                            .exists())

                    .andExpect(jsonPath("$.summary.departments_count")
                            .exists());
        }

        @Test
        @DisplayName("Should sort department costs by total cost")
        void shouldSortDepartmentCostsByTotalCost()
                throws Exception {

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

                    .andExpect(jsonPath("$.data[0]")
                            .exists())

                    .andExpect(jsonPath(
                            "$.data[0].total_cost"
                    ).exists());
        }

        @Test
        @DisplayName("Should return 400 for invalid analytics parameter")
        void shouldReturn400ForInvalidAnalyticsParameter()
                throws Exception {

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
        @DisplayName("Should return empty analytics when no active tools exist")
        void shouldReturnEmptyAnalyticsWhenNoActiveToolsExist()
                throws Exception {

            mockMvc.perform(
                            get("/api/analytics/department-costs")
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath("$.summary")
                            .exists());
        }
    }

    @Nested
    @DisplayName("Expensive tools analytics tests")
    class ExpensiveToolsAnalyticsTests {

        @Test
        @DisplayName("Should retrieve expensive tools from PostgreSQL")
        void shouldRetrieveExpensiveToolsFromPostgreSQL()
                throws Exception {

            mockMvc.perform(
                            get("/api/analytics/expensive-tools")
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath("$.analysis")
                            .exists())

                    .andExpect(jsonPath(
                            "$.analysis.total_tools_analyzed"
                    ).exists())

                    .andExpect(jsonPath(
                            "$.analysis.avg_cost_per_user_company"
                    ).exists())

                    .andExpect(jsonPath(
                            "$.analysis.potential_savings_identified"
                    ).exists());
        }

        @Test
        @DisplayName("Should filter expensive tools by min cost and limit")
        void shouldFilterExpensiveToolsByMinCostAndLimit()
                throws Exception {

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

                    .andExpect(jsonPath("$.data.length()")
                            .value(5))

                    .andExpect(jsonPath(
                            "$.data[0].monthly_cost"
                    ).exists());
        }

        @Test
        @DisplayName("Should return 400 for invalid limit parameter")
        void shouldReturn400ForInvalidLimitParameter()
                throws Exception {

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
                            .value(
                                    "Invalid analytics parameter"
                            ))

                    .andExpect(jsonPath("$.details.limit")
                            .value(
                                    "Must be positive integer between 1 and 100"
                            ))

                    .andExpect(jsonPath("$.timestamp")
                            .exists());
        }
    }

    @Nested
    @DisplayName("Tools by category analytics tests")
    class ToolsByCategoryAnalyticsTests {

        @Test
        @DisplayName(
                "Should retrieve tools by category from PostgreSQL"
        )
        void shouldRetrieveToolsByCategoryFromPostgreSQL()
                throws Exception {

            mockMvc.perform(
                            get(
                                    "/api/analytics/tools-by-category"
                            )
                    )

                    .andExpect(
                            status().isOk()
                    )

                    .andExpect(
                            jsonPath("$.data")
                                    .isArray()
                    )

                    .andExpect(
                            jsonPath("$.insights")
                                    .exists()
                    )

                    .andExpect(
                            jsonPath(
                                    "$.insights.most_expensive_category"
                            ).exists()
                    )

                    .andExpect(
                            jsonPath(
                                    "$.insights.most_efficient_category"
                            ).exists()
                    );
        }
    }
}