package com.techcorp.internaltoolsapi.controller;

import com.techcorp.internaltoolsapi.tools.dto.request.CreateToolRequest;
import com.techcorp.internaltoolsapi.tools.dto.request.UpdateToolRequest;
import com.techcorp.internaltoolsapi.tools.dto.response.PaginatedToolResponse;
import com.techcorp.internaltoolsapi.tools.dto.response.ToolDetailsResponse;
import com.techcorp.internaltoolsapi.tools.dto.response.ToolResponse;
import com.techcorp.internaltoolsapi.tools.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.tools.entity.enums.ToolStatusType;
import com.techcorp.internaltoolsapi.tools.service.ToolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST controller exposing tool management endpoints.
 */
@RestController
@RequestMapping("/api/tools")
@Validated
@Tag(
        name = "Tools",
        description = """
                REST endpoints for internal SaaS
                tools management, lifecycle updates
                and operational tracking.
                """
)
public class ToolController {

    // ---------- ATTRIBUTES ---------- //

    private final ToolService toolService;

    // ---------- CONSTRUCTORS ---------- //

    @Autowired
    public ToolController(
            ToolService toolService
    ) {
        this.toolService = toolService;
    }

    // ---------- ENDPOINTS ---------- //

    /**
     * Retrieves tools using optional filtering,
     * pagination and sorting support.
     *
     * @param department optional department filter
     * @param status     optional status filter
     * @param category   optional category filter
     * @param vendor     optional vendor filter
     * @param name       optional partial name filter
     * @param minCost    optional minimum monthly cost
     * @param maxCost    optional maximum monthly cost
     * @return paginated response containing:
     * - filtered tools
     * - applied filters
     * - pagination metadata
     * - sorting metadata
     */
    @GetMapping
    @Operation(
            summary = "Retrieve tools with optional filtering",
            description = """
                    Returns internal SaaS tools with optional filtering support.
                    
                    Supported filters:
                    - department (exact enum match)
                    - status (exact enum match)
                    - category (case-insensitive exact match)
                    - vendor (case-insensitive exact match)
                    - name (case-insensitive partial match)
                    - min_cost (inclusive minimum monthly cost)
                    - max_cost (inclusive maximum monthly cost)
                    
                    All filters are optional and combinable.
                    
                    Pagination support:
                    - page starts at 0
                    - limit default value is 10
                    
                    Sorting support:
                    - direction supports: asc, desc
                    
                    Allowed sorting fields:
                    - name
                    - vendor
                    - status
                    - monthlyCost
                    - activeUsersCount
                    - createdAt
                    - updatedAt
                    
                    Sorting fields are intentionally restricted
                    to preserve API contract stability
                    and avoid unsupported property access.
                    """
    )
    public PaginatedToolResponse getTools(
            @RequestParam(name = "department", required = false) DepartmentType department,
            @RequestParam(name = "status", required = false) ToolStatusType status,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "vendor", required = false) String vendor,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "min_cost", required = false) BigDecimal minCost,
            @RequestParam(name = "max_cost", required = false) BigDecimal maxCost,

            @Parameter(
                    description = "Page index starting from 0",
                    example = "0"
            )
            @RequestParam(name = "page", defaultValue = "0") int page,

            @Parameter(
                    description = "Maximum number of elements per page",
                    example = "10"
            )
            @RequestParam(name = "limit", defaultValue = "10") int limit,

            @Parameter(
                    description = """
                            Sorting field.
                            Allowed values:
                            name,
                            vendor,
                            status,
                            monthlyCost,
                            activeUsersCount,
                            createdAt,
                            updatedAt
                            """,
                    example = "createdAt"
            )
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,

            @Parameter(
                    description = "Sorting direction: asc or desc",
                    example = "desc"
            )
            @RequestParam(name = "direction", defaultValue = "desc") String direction
    ) {

        PaginatedToolResponse response =
                toolService.getToolsWithFilters(
                        department,
                        status,
                        category,
                        vendor,
                        name,
                        minCost,
                        maxCost,
                        page,
                        limit,
                        sort,
                        direction
                );

        return response;
    }

    /**
     * Retrieves detailed information
     * about a specific internal tool.
     * <p>
     * A ResourceNotFoundException is thrown
     * if the tool does not exist.
     *
     * @param id tool identifier
     * @return tool response
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve a tool by ID",
            description = """
                    Returns detailed information
                    about a specific internal tool.
                    
                    The endpoint returns:
                    - tool metadata
                    - category
                    - ownership
                    - usage information
                    - cost information
                    """
    )
    public ToolDetailsResponse getToolById(
            @PathVariable
            @Positive(
                    message = "Tool ID must be positive"
            )
            Integer id
    ) {

        ToolDetailsResponse tool =
                toolService.getToolById(id);

        return tool;
    }

    /**
     * Creates a new internal tool
     * and returns HTTP 201 Created.
     * <p>
     * Default values:
     * - status = active
     * - active_users_count = 0
     * <p>
     * A DuplicateResourceException is thrown
     * if a tool with the same name already exists.
     *
     * @param request tool creation payload
     * @return created tool response
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new tool",
            description = """
                    Creates a new internal SaaS tool.
                    
                    The endpoint validates:
                    - unique tool name
                    - category existence
                    - enum values
                    
                    Default values:
                    - status = active
                    - active_users_count = 0
                    
                    Request and response payloads
                    use snake_case JSON naming.
                    """
    )
    public ToolResponse createTool(
            @Valid
            @RequestBody
            CreateToolRequest request
    ) {

        ToolResponse createdTool =
                toolService.createTool(
                        request
                );

        return createdTool;
    }

    /**
     * Updates an existing tool.
     * <p>
     * Only provided fields are updated.
     * Fields omitted from the request remain unchanged.
     * <p>
     * An empty update request is accepted
     * but does not modify the existing entity.
     * <p>
     * A ResourceNotFoundException is thrown
     * if the target tool does not exist.
     *
     * @param id      tool ID
     * @param request partial update payload
     * @return updated tool response
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing tool",
            description = """
                    Updates an existing internal SaaS tool.
                    
                    Only provided fields are updated.
                    Fields omitted from the request
                    remain unchanged.
                    
                    An empty request body is accepted
                    but results in no data modification.
                    
                    The endpoint validates:
                    - tool existence
                    - category existence
                    - enum values
                    
                    Request and response payloads
                    use snake_case JSON naming.
                    """
    )
    public ToolResponse updateTool(
            @PathVariable
            @Positive(message = "Tool ID must be positive")
            Integer id,

            @Valid
            @RequestBody
            UpdateToolRequest request
    ) {

        ToolResponse updatedTool = toolService.updateTool(id, request);

        return updatedTool;
    }

    /**
     * Deletes an existing internal tool.
     * <p>
     * The operation is irreversible
     * and permanently removes the tool.
     * <p>
     * Returns HTTP 204 No Content
     * when deletion succeeds.
     *
     * @param id tool ID
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete an existing tool",
            description = """
                    Deletes an internal SaaS tool.
                    
                    The endpoint validates:
                    - tool existence
                    
                    The operation is irreversible
                    and permanently removes the tool.
                    """
    )
    public void deleteTool(
            @PathVariable
            @Positive(message = "Tool ID must be positive")
            Integer id
    ) {

        toolService.deleteTool(id);
    }
}