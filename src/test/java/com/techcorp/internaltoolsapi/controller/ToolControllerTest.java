package com.techcorp.internaltoolsapi.controller;

import com.techcorp.internaltoolsapi.dto.request.UpdateToolRequest;
import com.techcorp.internaltoolsapi.dto.response.ToolResponse;
import com.techcorp.internaltoolsapi.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.entity.enums.ToolStatusType;
import com.techcorp.internaltoolsapi.exception.ResourceNotFoundException;
import com.techcorp.internaltoolsapi.service.ToolService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.techcorp.internaltoolsapi.dto.request.CreateToolRequest;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

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
                    null
            )).thenReturn(List.of(createMockToolResponse()));

            mockMvc.perform(get("/api/tools"))

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.success")
                            .value(true))

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath("$.data.length()")
                            .value(1));

            verify(toolService).getToolsWithFilters(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
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
                    null
            )).thenReturn(List.of(createMockToolResponse()));

            mockMvc.perform(
                            get("/api/tools")
                                    .param("category", "Development")
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.success")
                            .value(true));

            verify(toolService).getToolsWithFilters(
                    null,
                    null,
                    "Development",
                    null,
                    null,
                    null,
                    null
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
                    new BigDecimal("50")
            )).thenReturn(List.of(createMockToolResponse()));

            mockMvc.perform(
                            get("/api/tools")
                                    .param("department", "Engineering")
                                    .param("status", "active")
                                    .param("category", "Development")
                                    .param("min_cost", "10")
                                    .param("max_cost", "50")
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.success")
                            .value(true));

            verify(toolService).getToolsWithFilters(
                    DepartmentType.Engineering,
                    ToolStatusType.active,
                    "Development",
                    null,
                    null,
                    new BigDecimal("10"),
                    new BigDecimal("50")
            );
        }

        @Test
        @DisplayName("Should return tool by ID")
        void shouldReturnToolById()
                throws Exception {

            when(toolService.getToolById(1))
                    .thenReturn(createMockToolResponse());

            mockMvc.perform(get("/api/tools/1"))

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.success")
                            .value(true))

                    .andExpect(jsonPath("$.message")
                            .value("Tool retrieved successfully"))

                    .andExpect(jsonPath("$.data.id")
                            .value(1))

                    .andExpect(jsonPath("$.data.name")
                            .value("Slack"));
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

                    .andExpect(jsonPath("$.success")
                            .value(false))

                    .andExpect(jsonPath("$.error")
                            .value("Resource not found"))

                    .andExpect(jsonPath("$.message")
                            .value("Tool with ID 999999 does not exist"));
        }

        @Test
        @DisplayName("Should return 400 for invalid ID")
        void shouldReturn400ForInvalidId()
                throws Exception {

            mockMvc.perform(get("/api/tools/-1"))

                    .andExpect(status().isBadRequest())

                    .andExpect(jsonPath("$.success")
                            .value(false))

                    .andExpect(jsonPath("$.error")
                            .value("Validation failed"))

                    .andExpect(jsonPath("$.message")
                            .value("Invalid request parameters"));
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

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.success")
                            .value(true))

                    .andExpect(jsonPath("$.message")
                            .value("Tool created successfully"))

                    .andExpect(jsonPath("$.data.name")
                            .value("LinearTest"));
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

                    .andExpect(jsonPath("$.success")
                            .value(true))

                    .andExpect(jsonPath("$.message")
                            .value("Tool updated successfully"))

                    .andExpect(jsonPath("$.data.name")
                            .value("Slack Enterprise"));
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

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.success")
                            .value(true))

                    .andExpect(jsonPath("$.message")
                            .value("Tool deleted successfully"))

                    .andExpect(jsonPath("$.data")
                            .doesNotExist());
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

                    .andExpect(status().isNotFound());
        }
    }
}