package com.techcorp.internaltoolsapi.service;

import com.techcorp.internaltoolsapi.dto.request.CreateToolRequest;
import com.techcorp.internaltoolsapi.dto.response.ToolResponse;

import java.util.List;

/**
 * Service responsible for tool business operations.
 */
public interface ToolService {

    /**
     * Retrieves all available tools.
     *
     * @return list of tool responses
     */
    List<ToolResponse> getAllTools();

    /**
     * Retrieves a tool by its identifier.
     *
     * @param id tool identifier
     * @return tool response
     */
    ToolResponse getToolById(
            Integer id
    );

    /**
     * Creates a new tool.
     *
     * @param request tool creation payload
     * @return created tool response
     */
    ToolResponse createTool(
            CreateToolRequest request
    );
}