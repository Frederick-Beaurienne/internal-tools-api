package com.techcorp.internaltoolsapi.domain.tools.service;

import com.techcorp.internaltoolsapi.domain.tools.dto.request.CreateToolRequest;
import com.techcorp.internaltoolsapi.domain.tools.dto.request.UpdateToolRequest;
import com.techcorp.internaltoolsapi.domain.tools.dto.response.PaginatedToolResponse;
import com.techcorp.internaltoolsapi.domain.tools.dto.response.ToolDetailsResponse;
import com.techcorp.internaltoolsapi.domain.tools.dto.response.ToolResponse;
import com.techcorp.internaltoolsapi.domain.tools.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.domain.tools.entity.enums.ToolStatusType;

import java.math.BigDecimal;

/**
 * Service responsible for internal tool
 * business operations and lifecycle management.
 */
public interface ToolService {

    /**
     * Retrieves tools using optional filtering criteria,
     * pagination and sorting support.
     * <p>
     * Supported filters:
     * - department exact match
     * - status exact match
     * - category exact match (case-insensitive)
     * - vendor exact match (case-insensitive)
     * - name partial match (case-insensitive)
     * - monthly cost range filtering
     * <p>
     * Supported sorting fields:
     * - name
     * - vendor
     * - status
     * - monthlyCost
     * - activeUsersCount
     * - createdAt
     * - updatedAt
     * <p>
     * Sorting direction:
     * - asc
     * - desc
     * <p>
     * Sorting fields are intentionally restricted
     * to a controlled whitelist in order to:
     * - avoid invalid or unsupported property access
     * - preserve API contract stability
     * - prevent exposing unintended internal fields
     * - keep sorting behavior predictable and maintainable
     * <p>
     * All filters are optional and combinable.
     *
     * @param department optional department filter
     * @param status     optional status filter
     * @param category   optional category filter
     * @param vendor     optional vendor filter
     * @param name       optional partial name filter
     * @param minCost    optional minimum monthly cost
     * @param maxCost    optional maximum monthly cost
     * @param page       page index starting from 0
     * @param limit      number of elements per page
     * @param sort       sorting field
     * @param direction  sorting direction
     * @return paginated response containing:
     * - filtered tools
     * - applied filters
     * - pagination metadata
     * - sorting metadata
     */
    PaginatedToolResponse getToolsWithFilters(
            DepartmentType department,
            ToolStatusType status,
            String category,
            String vendor,
            String name,
            BigDecimal minCost,
            BigDecimal maxCost,
            int page,
            int limit,
            String sort,
            String direction
    );

    /**
     * Retrieves a tool by its identifier.
     *
     * @param id tool identifier
     * @return tool response
     */
    ToolDetailsResponse getToolById(
            Integer id
    );

    /**
     * Creates a new tool.
     * <p>
     * Default values:
     * - status = active
     * - active users count = 0
     * <p>
     * A DuplicateResourceException is thrown
     * if a tool with the same name already exists.
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
     * <p>
     * A ResourceNotFoundException is thrown
     * if the target tool does not exist.
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
     * Deletes an existing internal tool.
     * <p>
     * The operation is irreversible.
     * <p>
     * A ResourceNotFoundException is thrown
     * if the target tool does not exist.
     *
     * @param id tool ID
     */
    void deleteTool(Integer id);
}