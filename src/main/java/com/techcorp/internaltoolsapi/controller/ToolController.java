package com.techcorp.internaltoolsapi.controller;

import com.techcorp.internaltoolsapi.dto.request.CreateToolRequest;
import com.techcorp.internaltoolsapi.dto.request.UpdateToolRequest;
import com.techcorp.internaltoolsapi.dto.response.ApiResponse;
import com.techcorp.internaltoolsapi.dto.response.ToolResponse;
import com.techcorp.internaltoolsapi.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.entity.enums.ToolStatusType;
import com.techcorp.internaltoolsapi.service.ToolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
     * Retrieves tools with optional filtering support.
     *
     * @param department optional department filter
     * @param status     optional status filter
     * @param category   optional category filter
     * @param vendor     optional vendor filter
     * @param name       optional partial name filter
     * @param minCost    optional minimum monthly cost
     * @param maxCost    optional maximum monthly cost
     * @return filtered tool list
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
                    
                    Pagination and sorting are intentionally omitted
                    to keep the API focused on the technical
                    requirements of the exercise.
                    """
    )
    public ApiResponse<List<ToolResponse>> getTools(
            @RequestParam(name = "department", required = false) DepartmentType department,
            @RequestParam(name = "status", required = false) ToolStatusType status,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "vendor", required = false) String vendor,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "min_cost", required = false) BigDecimal minCost,
            @RequestParam(name = "max_cost", required = false) BigDecimal maxCost
    ) {

        List<ToolResponse> tools =
                toolService.getToolsWithFilters(
                        department,
                        status,
                        category,
                        vendor,
                        name,
                        minCost,
                        maxCost
                );

        return ApiResponse.success(
                tools,
                "Tools retrieved successfully"
        );
    }


    /**
     * Retrieves a tool by its identifier.
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
    public ApiResponse<ToolResponse> getToolById(

            @PathVariable
            @Positive(
                    message = "Tool ID must be positive"
            )
            Integer id
    ) {

        ToolResponse tool =
                toolService.getToolById(id);

        return ApiResponse.success(
                tool,
                "Tool retrieved successfully"
        );
    }

    /**
     * Creates a new internal tool.
     *
     * @param request tool creation payload
     * @return created tool response
     */
    @PostMapping
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
    public ApiResponse<ToolResponse> createTool(

            @Valid
            @RequestBody
            CreateToolRequest request
    ) {

        ToolResponse createdTool =
                toolService.createTool(
                        request
                );

        return ApiResponse.success(
                createdTool,
                "Tool created successfully"
        );
    }

    /**
     * Updates an existing tool.
     * <p>
     * Only provided fields are updated.
     * Fields omitted from the request remain unchanged.
     * <p>
     * An empty update request is accepted
     * but does not modify the existing entity.
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
    public ApiResponse<ToolResponse> updateTool(
            @PathVariable
            @Positive(message = "Tool ID must be positive")
            Integer id,

            @Valid
            @RequestBody
            UpdateToolRequest request
    ) {

        ToolResponse updatedTool = toolService.updateTool(id, request);

        return ApiResponse.success(
                updatedTool,
                "Tool updated successfully"
        );
    }

    /**
     * Deletes an existing internal tool.
     *
     * @param id tool ID
     * @return success response
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an existing tool",
            description = """
                    Deletes an internal SaaS tool.
                    
                    The endpoint validates:
                    - tool existence
                    
                    The operation is irreversible.
                    """
    )
    public ApiResponse<Object> deleteTool(
            @PathVariable
            @Positive(message = "Tool ID must be positive")
            Integer id
    ) {

        toolService.deleteTool(id);

        return ApiResponse.success(
                null,
                "Tool deleted successfully"
        );
    }
}