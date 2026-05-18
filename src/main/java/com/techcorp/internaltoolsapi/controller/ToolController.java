package com.techcorp.internaltoolsapi.controller;

import com.techcorp.internaltoolsapi.dto.request.CreateToolRequest;
import com.techcorp.internaltoolsapi.dto.request.UpdateToolRequest;
import com.techcorp.internaltoolsapi.dto.response.ApiResponse;
import com.techcorp.internaltoolsapi.dto.response.ToolResponse;
import com.techcorp.internaltoolsapi.service.ToolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
                Endpoints for internal SaaS tools
                management and retrieval.
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
     * Retrieves all available tools.
     *
     * @return list of tool responses
     */
    @GetMapping
    @Operation(
            summary = "Retrieve all tools",
            description = """
                    Returns all internal SaaS tools
                    available in the platform.
                    
                    Includes:
                    - category
                    - vendor
                    - status
                    - department ownership
                    - monthly cost
                    """
    )
    public ApiResponse<List<ToolResponse>> getAllTools() {

        List<ToolResponse> tools =
                toolService.getAllTools();

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
                - required fields
                - enum values
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
     * Updates an existing internal tool.
     *
     * @param id tool ID
     * @param request update payload
     * @return updated tool response
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing tool",
            description = """
                Updates an existing internal SaaS tool.

                The endpoint validates:
                - tool existence
                - unique tool name
                - category existence
                - required fields
                - enum values
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