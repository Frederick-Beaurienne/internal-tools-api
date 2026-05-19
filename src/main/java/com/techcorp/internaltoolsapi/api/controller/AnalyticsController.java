package com.techcorp.internaltoolsapi.api.controller;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

/**
 * REST controller exposing analytics
 * and reporting endpoints.
 */
@RestController
@RequestMapping("/api/analytics")
@Validated
@Tag(
        name = "Analytics",
        description = """
                REST endpoints exposing
                analytics, reporting and
                cost optimization insights
                for internal SaaS tools.

                Global analytics rules:
                - only active tools are included
                - financial values use controlled rounding
                - analytics provide business-oriented
                  insights and optimization metrics
                """
)
public class AnalyticsController {

    // ---------- DEPENDENCIES ---------- //

    private final AnalyticsService analyticsService;

    // ---------- CONSTRUCTORS ---------- //

    @Autowired
    public AnalyticsController(
            AnalyticsService analyticsService
    ) {
        this.analyticsService =
                analyticsService;
    }

    // ---------- ENDPOINTS ---------- //

    /**
     * Retrieves department-level
     * cost distribution analytics.
     *
     * @param sortBy optional sorting field
     * @param order optional sorting direction
     * @return department cost analytics
     */
    @GetMapping("/department-costs")
    @Operation(
            summary = "Retrieve department cost analytics",
            description = """
                Returns department-level
                cost analytics and company
                budget distribution insights.

                Analytics include:
                - total department costs
                - tools count
                - users count
                - average cost per tool
                - budget percentages
                - company-level summary

                Global analytics rules:
                - only active tools are included
                - departments without active tools
                  are returned with zero metrics
                - empty datasets return
                  an explicit analytics message
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = """
                Department analytics
                successfully retrieved.
                May return either:
                - populated analytics
                - empty analytics response
                """,
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "Default analytics response",
                                            summary = "Department analytics with active tools",
                                            value = """
                                        {
                                          "data": [
                                            {
                                              "department": "Engineering",
                                              "total_cost": 2495.50,
                                              "tools_count": 7,
                                              "total_users": 329,
                                              "average_cost_per_tool": 356.50,
                                              "cost_percentage": 81.0
                                            }
                                          ],
                                          "summary": {
                                            "total_company_cost": 3081.64,
                                            "departments_count": 7,
                                            "most_expensive_department": "Engineering"
                                          }
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Empty analytics response",
                                            summary = "No active analytics data available",
                                            value = """
                                        {
                                          "data": [],
                                          "message": "No analytics data available - ensure tools data exists",
                                          "summary": {
                                            "total_company_cost": 0,
                                            "departments_count": 0,
                                            "most_expensive_department": null
                                          }
                                        }
                                        """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                        Invalid analytics
                        parameters supplied.
                        Examples:
                        invalid sort_by
                        invalid order
                        """
            )
    })
    public DepartmentCostsResponse getDepartmentCosts(

            @Parameter(
                    description = """
                            Sorting field.
                            Allowed values:
                            total_cost,
                            department
                            """,
                    example = "total_cost"
            )
            @RequestParam(
                    name = "sort_by",
                    required = false
            )
            String sortBy,

            @Parameter(
                    description = """
                            Sorting direction.
                            Allowed values:
                            asc,
                            desc
                            """,
                    example = "desc"
            )
            @RequestParam(
                    name = "order",
                    required = false
            )
            String order
    ) {

        DepartmentCostsResponse response =
                analyticsService.getDepartmentCosts(
                        sortBy,
                        order
                );

        return response;
    }
}