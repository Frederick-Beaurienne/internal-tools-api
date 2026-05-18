package com.techcorp.internaltoolsapi.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    // ---------- TESTS ---------- //

    @Test
    @DisplayName("Doit récupérer les outils depuis la vraie base PostgreSQL")
    void shouldRetrieveToolsFromRealDatabase()
            throws Exception {

        mockMvc.perform(
                        get("/api/tools")
                )

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success")
                        .value(true))

                .andExpect(jsonPath("$.data")
                        .isArray());
    }

    @Test
    @DisplayName("Should create tool using PostgreSQL enums")
    void shouldCreateTool()
            throws Exception {

        String requestBody = """
                {
                  "name": "LinearTest",
                  "description": "Issue tracking platform",
                  "vendor": "Linear",
                  "websiteUrl": "https://linear.app",
                  "categoryId": 1,
                  "monthlyCost": 250.00,
                  "ownerDepartment": "Engineering",
                  "status": "active",
                  "activeUsersCount": 40
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

                .andExpect(jsonPath("$.data.ownerDepartment")
                        .value("Engineering"))

                .andExpect(jsonPath("$.data.status")
                        .value("active"));
    }
}