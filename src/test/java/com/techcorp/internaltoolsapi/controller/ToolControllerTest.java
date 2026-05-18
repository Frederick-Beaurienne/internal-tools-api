package com.techcorp.internaltoolsapi.controller;

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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    // ---------- TEST GROUPS ---------- //

    @Nested
    @DisplayName("Tests API de récupération des outils")
    class GetToolsApiTests {

        @Test
        @DisplayName("Doit retourner tous les outils")
        void shouldReturnAllTools()
                throws Exception {

            when(toolService.getAllTools())
                    .thenReturn(
                            List.of(createMockToolResponse())
                    );

            mockMvc.perform(
                            get("/api/tools")
                    )

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.success")
                            .value(true))

                    .andExpect(jsonPath("$.message")
                            .value("Tools retrieved successfully"))

                    .andExpect(jsonPath("$.data")
                            .isArray())

                    .andExpect(jsonPath("$.data[0].name")
                            .value("Slack"));
        }

        @Test
        @DisplayName("Doit retourner un outil par son ID")
        void shouldReturnToolById()
                throws Exception {

            when(toolService.getToolById(1))
                    .thenReturn(
                            createMockToolResponse()
                    );

            mockMvc.perform(
                            get("/api/tools/1")
                    )

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
        @DisplayName("Doit retourner une erreur 404 si l'outil est introuvable")
        void shouldReturn404WhenToolDoesNotExist()
                throws Exception {

            when(toolService.getToolById(999999))
                    .thenThrow(
                            new ResourceNotFoundException(
                                    "Tool with ID 999999 does not exist"
                            )
                    );

            mockMvc.perform(
                            get("/api/tools/999999")
                    )

                    .andExpect(status().isNotFound())

                    .andExpect(jsonPath("$.success")
                            .value(false))

                    .andExpect(jsonPath("$.error")
                            .value("Resource not found"))

                    .andExpect(jsonPath("$.message")
                            .value("Tool with ID 999999 does not exist"));
        }

        @Test
        @DisplayName("Doit retourner une erreur 400 pour un ID invalide")
        void shouldReturn400ForInvalidId()
                throws Exception {

            mockMvc.perform(
                            get("/api/tools/-1")
                    )

                    .andExpect(status().isBadRequest())

                    .andExpect(jsonPath("$.success")
                            .value(false))

                    .andExpect(jsonPath("$.error")
                            .value("Validation failed"))

                    .andExpect(jsonPath("$.message")
                            .value("Invalid request parameters"));
        }
    }
}