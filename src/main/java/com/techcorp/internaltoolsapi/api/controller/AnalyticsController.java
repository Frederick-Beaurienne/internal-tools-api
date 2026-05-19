package com.techcorp.internaltoolsapi.api.controller;

import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.expensivetools.ExpensiveToolsResponse;
import com.techcorp.internaltoolsapi.domain.analytics.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
     * @param order  optional sorting direction
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

    /**
     * Retrieves expensive
     * tools analytics.
     *
     * @param minCost optional minimum cost
     * @param limit   maximum tools returned
     * @return expensive tools analytics
     */
    @GetMapping("/expensive-tools")
    @Operation(
            summary = "Retrieve expensive tools analytics",
            description = """
                    Returns expensive
                    tools analytics and
                    optimization insights.
                    
                    Analytics include:
                    - expensive tools ranking
                    - cost per user
                    - efficiency rating
                    - company comparison
                    - company-wide cpu benchmark
                    - potential savings
                    
                    Global analytics rules:
                    - only active tools are included
                    - tools are ordered by
                      monthly cost descending
                    - cost calculations use
                      controlled rounding
                    - division by zero
                      is safely handled
                    - tools with zero users
                      receive the
                      not_applicable
                      efficiency rating
                    - empty datasets return
                      an explicit analytics message
                    - company average cpu
                      uses weighted
                      enterprise calculation
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = """
                            Expensive tools analytics
                            successfully retrieved.
                            May return either:
                            - populated analytics
                            - empty analytics response
                            """,
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "Default expensive tools response",
                                            summary = "Expensive tools analytics",
                                            value = """
                                                    {
                                                      "data": [
                                                        {
                                                          "id": 1,
                                                          "name": "GitHub Enterprise",
                                                          "monthly_cost": 1200.00,
                                                          "active_users_count": 200,
                                                          "cost_per_user": 6.00,
                                                          "department": "Engineering",
                                                          "vendor": "GitHub",
                                                          "efficiency_rating": "excellent"
                                                        },
                                                        {
                                                          "id": 23,
                                                          "name": "Premium Design Tools",
                                                          "monthly_cost": 45.00,
                                                          "active_users_count": 0,
                                                          "cost_per_user": 0,
                                                          "department": "Design",
                                                          "vendor": "DesignPro",
                                                          "efficiency_rating": "not_applicable"
                                                        }
                                                      ],
                                                      "analysis": {
                                                        "total_tools_analyzed": 10,
                                                        "avg_cost_per_user_company": 9.50,
                                                        "potential_savings_identified": 1250.00
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Empty expensive tools response",
                                            summary = "No analytics data available",
                                            value = """
                                                    {
                                                      "data": [],
                                                      "message": "No analytics data available - ensure tools data exists",
                                                      "analysis": {
                                                        "total_tools_analyzed": 0,
                                                        "avg_cost_per_user_company": 0,
                                                        "potential_savings_identified": 0
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
                            invalid limit
                            invalid min_cost
                            """
            )
    })
    public ExpensiveToolsResponse getExpensiveTools(

            @Parameter(
                    description = """
                            Minimum monthly cost.
                            Only tools with
                            monthly_cost >= value
                            are returned.
                            """,
                    example = "100"
            )
            @RequestParam(
                    name = "min_cost",
                    required = false
            )
            BigDecimal minCost,

            @Parameter(
                    description = """
                            Maximum tools returned.
                            Allowed range:
                            1 to 100
                            """,
                    example = "10"
            )
            @RequestParam(
                    name = "limit",
                    required = false
            )
            Integer limit
    ) {

        ExpensiveToolsResponse response =
                analyticsService.getExpensiveTools(
                        minCost,
                        limit
                );

        return response;
    }
}