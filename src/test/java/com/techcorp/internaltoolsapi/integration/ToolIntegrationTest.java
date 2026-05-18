package com.techcorp.internaltoolsapi.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Full integration tests for tool API workflow.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ToolIntegrationTest {

    // ---------- DEPENDENCIES ---------- //

    @Autowired
    private MockMvc mockMvc;

    // ---------- TEST GROUPS ---------- //

    @Nested
    @DisplayName("Read operations tests")
    class ReadOperationsTests {

        @Test
        @DisplayName("Should retrieve tools from real PostgreSQL database")
        void shouldRetrieveToolsFromRealDatabase()
                throws Exception {

            mockMvc.perform(get("/api/tools"))

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.success")
                            .value(true))

                    .andExpect(jsonPath("$.data")
                            .isArray());
        }
    }

    @Nested
    @DisplayName("Write operations tests")
    class WriteOperationsTests {

        @Test
        @DisplayName("Should create tool using PostgreSQL enums")
        void shouldCreateTool()
                throws Exception {

            String requestBody = """
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

            mockMvc.perform(
                            post("/api/tools")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.success")
                            .value(true))

                    .andExpect(jsonPath("$.message")
                            .value("Tool created successfully"))

                    .andExpect(jsonPath("$.data.name")
                            .value("LinearTest"))

                    .andExpect(jsonPath("$.data.owner_department")
                            .value("Engineering"))

                    .andExpect(jsonPath("$.data.status")
                            .value("active"));
        }

        @Test
        @DisplayName("Should update tool using PostgreSQL enums")
        void shouldUpdateTool()
                throws Exception {

            String requestBody = """
        {
          "monthly_cost": 1800.00,
          "status": "active",
          "description": "Updated communication platform"
        }
        """;

            mockMvc.perform(
                            put("/api/tools/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody)
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.success")
                            .value(true))

                    .andExpect(jsonPath("$.data.name")
                            .value("Slack Enterprise"))

                    .andExpect(jsonPath("$.data.owner_department")
                            .value("Engineering"))

                    .andExpect(jsonPath("$.data.status")
                            .value("active"));
        }

        @Test
        @DisplayName("Should delete tool from PostgreSQL database")
        void shouldDeleteTool()
                throws Exception {

            mockMvc.perform(delete("/api/tools/1"))

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.success")
                            .value(true))

                    .andExpect(jsonPath("$.message")
                            .value("Tool deleted successfully"));
        }

        @Test
        @DisplayName("Should return 404 when deleting unknown tool")
        void shouldReturn404WhenDeletingUnknownTool()
                throws Exception {

            mockMvc.perform(delete("/api/tools/999999"))

                    .andExpect(status().isNotFound())

                    .andExpect(jsonPath("$.success")
                            .value(false))

                    .andExpect(jsonPath("$.error")
                            .value("Resource not found"));
        }
    }
}