package com.techcorp.internaltoolsapi.service;

import com.techcorp.internaltoolsapi.dto.request.CreateToolRequest;
import com.techcorp.internaltoolsapi.dto.request.UpdateToolRequest;
import com.techcorp.internaltoolsapi.dto.response.ToolResponse;
import com.techcorp.internaltoolsapi.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.entity.enums.ToolStatusType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service responsible for internal tool
 * business operations and lifecycle management.
 */
public interface ToolService {

    /**
     * Retrieves tools using optional filtering criteria.
     * <p>
     * Supported filters:
     * - department exact match
     * - status exact match
     * - category exact match (case-insensitive)
     * - vendor exact match (case-insensitive)
     * - name partial match (case-insensitive)
     * - monthly cost range filtering
     * <p>
     * All filters are optional and combinable.
     * <p>
     * Pagination and sorting are intentionally omitted
     * to keep the implementation focused on the
     * requirements of the exercise.
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
    List<ToolResponse> getToolsWithFilters(
            DepartmentType department,
            ToolStatusType status,
            String category,
            String vendor,
            String name,
            BigDecimal minCost,
            BigDecimal maxCost
    );

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
     * <p>
     * Default values:
     * - status = active
     * - active users count = 0
     *
     * @param request tool creation payload
     * @return created tool response
     */
    ToolResponse createTool(
            CreateToolRequest request
    );

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
    ToolResponse updateTool(
            Integer id,
            UpdateToolRequest request
    );

    /**
     * Deletes an existing tool.
     *
     * @param id tool ID
     */
    void deleteTool(Integer id);
}