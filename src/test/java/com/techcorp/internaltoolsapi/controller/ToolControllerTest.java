package com.techcorp.internaltoolsapi.controller;

import com.techcorp.internaltoolsapi.tools.dto.request.CreateToolRequest;
import com.techcorp.internaltoolsapi.tools.dto.request.UpdateToolRequest;
import com.techcorp.internaltoolsapi.tools.dto.response.PaginatedToolResponse;
import com.techcorp.internaltoolsapi.tools.dto.response.ToolDetailsResponse;
import com.techcorp.internaltoolsapi.tools.dto.response.ToolResponse;
import com.techcorp.internaltoolsapi.tools.dto.response.metadata.PaginationMetadata;
import com.techcorp.internaltoolsapi.tools.dto.response.metadata.SortingMetadata;
import com.techcorp.internaltoolsapi.analytics.dto.response.UsageMetricsResponse;
import com.techcorp.internaltoolsapi.analytics.dto.response.UsagePeriodMetricsResponse;
import com.techcorp.internaltoolsapi.tools.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.tools.entity.enums.ToolStatusType;
import com.techcorp.internaltoolsapi.exception.ResourceNotFoundException;
import com.techcorp.internaltoolsapi.tools.service.ToolService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller tests for ToolController endpoints.
 */
@WebMvcTest(ToolController.class)
class ToolControllerTest {

    // ---------- DEPENDENCIES ---------- //

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ToolService toolService;

    // ---------- TEST DATA ---------- //

    private ToolResponse createMockToolResponse() {

        return new ToolResponse(
                1,
                "Slack",
                "Team communication platform",
                "Slack",
                "https://slack.com",
                "Communication",
                new BigDecimal("1200.00"),
                DepartmentType.Engineering,
                ToolStatusType.active,
                150,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }

    private PaginatedToolResponse createMockPaginatedResponse(
            Map<String, Object> filtersApplied
    ) {

        return new PaginatedToolResponse(
                List.of(createMockToolResponse()),
                1,
                1,
                filtersApplied,
                new PaginationMetadata(
                        0,
                        10,
                        1,
                        true,
                        true
                ),
                new SortingMetadata(
                        "createdAt",
                        "desc"
                )
        );
    }

    private String createUpdateToolRequestJson() {

        return """
                {
                  "monthly_cost": 1800.00,
                  "status": "active",
                  "description": "Updated enterprise communication platform"
                }
                """;
    }

    private String createCreateToolRequestJson() {

        return """
                {
                  "name": "LinearTest",
                  "description": "Issue tracking platform",
                  "vendor": "Linear",
                  "website_url": "https://linear.app",
                  "category_id": 1,
                  "monthly_cost": 250.00,
                  "owner_department": "Engineering",
                  "status": "active",
                  "active_users_count": 40
                }
                """;
    }

    // ---------- TEST GROUPS ---------- //

    @Nested
    @DisplayName("GET endpoints tests")
    class GetEndpointsTests {

        @Test
        @DisplayName("Should return all tools when no filters are provided")
        void shouldReturnAllToolsWhenNoFiltersAreProvided()
                throws Exception {

            when(toolService.getToolsWithFilters(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    0,
                    10,
                    "createdAt",
                    "desc"
            )).thenReturn(
                    createMockPaginatedResponse(
                            Map.of()
                    )
            );

            mockMvc.perform(get("/api/tools"))

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath("$.data.length()")
                            .value(1))

                    .andExpect(jsonPath("$.pagination.current_page")
                            .value(0))

                    .andExpect(jsonPath("$.sorting.sort_by")
                            .value("createdAt"));

            verify(toolService).getToolsWithFilters(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    0,
                    10,
                    "createdAt",
                    "desc"
            );
        }

        @Test
        @DisplayName("Should filter tools by category")
        void shouldFilterToolsByCategory()
                throws Exception {

            when(toolService.getToolsWithFilters(
                    null,
                    null,
                    "Development",
                    null,
                    null,
                    null,
                    null,
                    0,
                    10,
                    "createdAt",
                    "desc"
            )).thenReturn(
                    createMockPaginatedResponse(
                            Map.of("category", "Development")
                    )
            );

            mockMvc.perform(
                            get("/api/tools")
                                    .param("category", "Development")
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath("$.filters_applied.category")
                            .value("Development"));

            verify(toolService).getToolsWithFilters(
                    null,
                    null,
                    "Development",
                    null,
                    null,
                    null,
                    null,
                    0,
                    10,
                    "createdAt",
                    "desc"
            );
        }

        @Test
        @DisplayName("Should filter tools with multiple criteria")
        void shouldFilterToolsWithMultipleCriteria()
                throws Exception {

            when(toolService.getToolsWithFilters(
                    DepartmentType.Engineering,
                    ToolStatusType.active,
                    "Development",
                    null,
                    null,
                    new BigDecimal("10"),
                    new BigDecimal("50"),
                    0,
                    10,
                    "createdAt",
                    "desc"
            )).thenReturn(
                    createMockPaginatedResponse(
                            Map.of(
                                    "department", "Engineering",
                                    "status", "active",
                                    "category", "Development"
                            )
                    )
            );

            mockMvc.perform(
                            get("/api/tools")
                                    .param("department", "Engineering")
                                    .param("status", "active")
                                    .param("category", "Development")
                                    .param("min_cost", "10")
                                    .param("max_cost", "50")
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath("$.filters_applied.category")
                            .value("Development"))

                    .andExpect(jsonPath("$.filters_applied.department")
                            .value("Engineering"))

                    .andExpect(jsonPath("$.filters_applied.status")
                            .value("active"));

            verify(toolService).getToolsWithFilters(
                    DepartmentType.Engineering,
                    ToolStatusType.active,
                    "Development",
                    null,
                    null,
                    new BigDecimal("10"),
                    new BigDecimal("50"),
                    0,
                    10,
                    "createdAt",
                    "desc"
            );
        }

        @Test
        @DisplayName("Should return tool by ID")
        void shouldReturnToolById()
                throws Exception {

            ToolDetailsResponse response =
                    createMockToolDetailsResponse();

            when(toolService.getToolById(1))
                    .thenReturn(response);

            mockMvc.perform(get("/api/tools/1"))

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.id")
                            .value(1))

                    .andExpect(jsonPath("$.name")
                            .value("Slack"))

                    .andExpect(jsonPath("$.vendor")
                            .value("Slack"))

                    .andExpect(jsonPath("$.category")
                            .value("Communication"))

                    .andExpect(jsonPath("$.total_monthly_cost")
                            .value(80.0))

                    .andExpect(jsonPath(
                            "$.usage_metrics.last_30_days.total_sessions"
                    ).value(127))

                    .andExpect(jsonPath(
                            "$.usage_metrics.last_30_days.avg_session_minutes"
                    ).value(45));
        }

        @Test
        @DisplayName("Should return 404 when tool does not exist")
        void shouldReturn404WhenToolDoesNotExist()
                throws Exception {

            when(toolService.getToolById(999999))
                    .thenThrow(
                            new ResourceNotFoundException(
                                    "Tool with ID 999999 does not exist"
                            )
                    );

            mockMvc.perform(get("/api/tools/999999"))

                    .andExpect(status().isNotFound())

                    .andExpect(jsonPath("$.error")
                            .value("Resource not found"))

                    .andExpect(jsonPath("$.message")
                            .value("Tool with ID 999999 does not exist"))

                    .andExpect(jsonPath("$.timestamp")
                            .exists());
        }

        @Test
        @DisplayName("Should return 400 for invalid ID")
        void shouldReturn400ForInvalidId()
                throws Exception {

            mockMvc.perform(get("/api/tools/-1"))

                    .andExpect(status().isBadRequest())

                    .andExpect(jsonPath("$.error")
                            .value("Validation failed"))

                    .andExpect(jsonPath("$.message")
                            .value("Invalid request parameters"))

                    .andExpect(jsonPath("$.details")
                            .exists())

                    .andExpect(jsonPath("$.timestamp")
                            .exists());
        }
    }

    @Nested
    @DisplayName("POST endpoints tests")
    class CreateEndpointsTests {

        @Test
        @DisplayName("Should create a new tool")
        void shouldCreateTool()
                throws Exception {

            ToolResponse createdTool = createMockToolResponse();
            createdTool.setName("LinearTest");

            when(toolService.createTool(
                    any(CreateToolRequest.class)
            )).thenReturn(createdTool);

            mockMvc.perform(
                            post("/api/tools")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(createCreateToolRequestJson())
                    )

                    .andExpect(status().isCreated())

                    .andExpect(jsonPath("$.name")
                            .value("LinearTest"))

                    .andExpect(jsonPath("$.vendor")
                            .value("Slack"))

                    .andExpect(jsonPath("$.status")
                            .value("active"));
        }
    }

    @Nested
    @DisplayName("PUT endpoints tests")
    class UpdateEndpointsTests {

        @Test
        @DisplayName("Should update an existing tool")
        void shouldUpdateTool()
                throws Exception {

            ToolResponse updatedTool = createMockToolResponse();
            updatedTool.setName("Slack Enterprise");

            when(toolService.updateTool(
                    eq(1),
                    any(UpdateToolRequest.class)
            )).thenReturn(updatedTool);

            mockMvc.perform(
                            put("/api/tools/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(createUpdateToolRequestJson())
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.name")
                            .value("Slack Enterprise"))

                    .andExpect(jsonPath("$.status")
                            .value("active"))

                    .andExpect(jsonPath("$.vendor")
                            .value("Slack"));
        }
    }

    @Nested
    @DisplayName("DELETE endpoints tests")
    class DeleteEndpointsTests {
        @Test
        @DisplayName("Should delete an existing tool")
        void shouldDeleteTool()
                throws Exception {

            doNothing().when(toolService)
                    .deleteTool(1);

            mockMvc.perform(delete("/api/tools/1"))

                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should return 404 when deleting unknown tool")
        void shouldReturn404WhenDeletingUnknownTool()
                throws Exception {

            doThrow(
                    new ResourceNotFoundException(
                            "Tool with ID 999999 does not exist"
                    )
            ).when(toolService).deleteTool(999999);

            mockMvc.perform(delete("/api/tools/999999"))

                    .andExpect(status().isNotFound())

                    .andExpect(jsonPath("$.error")
                            .value("Resource not found"))

                    .andExpect(jsonPath("$.message")
                            .value("Tool with ID 999999 does not exist"))

                    .andExpect(jsonPath("$.timestamp")
                            .exists());
        }
    }

    private ToolDetailsResponse createMockToolDetailsResponse() {

        ToolDetailsResponse response =
                new ToolDetailsResponse();

        response.setId(1);
        response.setName("Slack");
        response.setDescription(
                "Team communication platform"
        );
        response.setVendor("Slack");
        response.setCategory("Communication");
        response.setMonthlyCost(
                BigDecimal.valueOf(8.00)
        );
        response.setOwnerDepartment(
                DepartmentType.Engineering
        );
        response.setStatus(
                ToolStatusType.active
        );
        response.setWebsiteUrl(
                "https://slack.com"
        );
        response.setActiveUsersCount(10);

        response.setTotalMonthlyCost(
                BigDecimal.valueOf(80.0)
        );

        UsagePeriodMetricsResponse period =
                new UsagePeriodMetricsResponse(
                        127,
                        45
                );

        UsageMetricsResponse metrics =
                new UsageMetricsResponse(
                        period
                );

        response.setUsageMetrics(metrics);

        return response;
    }
}