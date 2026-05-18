package com.techcorp.internaltoolsapi.tools.mapper;

import com.techcorp.internaltoolsapi.tools.dto.request.CreateToolRequest;
import com.techcorp.internaltoolsapi.tools.dto.request.UpdateToolRequest;
import com.techcorp.internaltoolsapi.tools.dto.response.ToolResponse;
import com.techcorp.internaltoolsapi.tools.entity.Category;
import com.techcorp.internaltoolsapi.tools.entity.Tool;
import com.techcorp.internaltoolsapi.tools.entity.enums.ToolStatusType;
import com.techcorp.internaltoolsapi.tools.dto.response.ToolDetailsResponse;
import com.techcorp.internaltoolsapi.analytics.dto.response.UsageMetricsResponse;
import com.techcorp.internaltoolsapi.analytics.dto.response.UsagePeriodMetricsResponse;

import java.math.BigDecimal;
/**
 * Mapper responsible for tool DTO conversions.
 */
public final class ToolMapper {

    private ToolMapper() {
    }

    /**
     * Maps Tool entity to ToolResponse DTO.
     *
     * @param tool tool entity
     * @return mapped response DTO
     */
    public static ToolResponse toResponse(
            Tool tool
    ) {

        return new ToolResponse(
                tool.getId(),
                tool.getName(),
                tool.getDescription(),
                tool.getVendor(),
                tool.getWebsiteUrl(),
                tool.getCategory() != null ? tool.getCategory().getName() : null,
                tool.getMonthlyCost(),
                tool.getOwnerDepartment(),
                tool.getStatus(),
                tool.getActiveUsersCount(),
                tool.getCreatedAt(),
                tool.getUpdatedAt()
        );
    }

    /**
     * Maps Tool entity to detailed response DTO.
     * <p>
     * Includes analytics and financial metrics
     * intended for detailed single-resource endpoints.
     *
     * @param tool tool entity
     * @return mapped detailed response DTO
     */
    public static ToolDetailsResponse toDetailsResponse(
            Tool tool
    ) {

        ToolResponse baseResponse =
                toResponse(tool);

        ToolDetailsResponse response =
                new ToolDetailsResponse();

        // ---------- BASE FIELDS ---------- //

        response.setId(baseResponse.getId());
        response.setName(baseResponse.getName());
        response.setDescription(baseResponse.getDescription());
        response.setVendor(baseResponse.getVendor());
        response.setWebsiteUrl(baseResponse.getWebsiteUrl());
        response.setCategory(baseResponse.getCategory());
        response.setMonthlyCost(baseResponse.getMonthlyCost());
        response.setOwnerDepartment(
                baseResponse.getOwnerDepartment()
        );
        response.setStatus(baseResponse.getStatus());
        response.setActiveUsersCount(
                baseResponse.getActiveUsersCount()
        );
        response.setCreatedAt(baseResponse.getCreatedAt());
        response.setUpdatedAt(baseResponse.getUpdatedAt());

        // ---------- FINANCIAL METRICS ---------- //

        if (
                tool.getMonthlyCost() != null
                        && tool.getActiveUsersCount() != null
        ) {

            BigDecimal totalMonthlyCost =
                    tool.getMonthlyCost()
                            .multiply(
                                    BigDecimal.valueOf(
                                            tool.getActiveUsersCount()
                                    )
                            );

            response.setTotalMonthlyCost(
                    totalMonthlyCost
            );
        }

        // ---------- MOCK USAGE METRICS ---------- //

        UsagePeriodMetricsResponse last30Days =
                new UsagePeriodMetricsResponse(
                        127,
                        45
                );

        UsageMetricsResponse usageMetrics =
                new UsageMetricsResponse(
                        last30Days
                );

        response.setUsageMetrics(
                usageMetrics
        );

        return response;
    }

    /**
     * Maps CreateToolRequest DTO to Tool entity.
     *
     * @param request  tool creation request
     * @param category resolved category entity
     * @return mapped tool entity
     */
    public static Tool toEntity(
            CreateToolRequest request,
            Category category
    ) {

        Tool tool = new Tool();

        tool.setName(request.getName());
        tool.setDescription(request.getDescription());
        tool.setVendor(request.getVendor());
        tool.setWebsiteUrl(request.getWebsiteUrl());
        tool.setCategory(category);
        tool.setMonthlyCost(request.getMonthlyCost());
        tool.setOwnerDepartment(request.getOwnerDepartment());

        tool.setStatus(
                request.getStatus() != null ?
                        request.getStatus() : ToolStatusType.active
        );
        tool.setActiveUsersCount(
                request.getActiveUsersCount() != null ?
                        request.getActiveUsersCount() : 0
        );

        return tool;
    }

    /**
     * Updates an existing tool entity from request DTO.
     * Only provided fields are updated.
     *
     * @param tool existing tool entity
     * @param request update request
     * @param category resolved category entity
     */
    public static void updateEntity(
            Tool tool,
            UpdateToolRequest request,
            Category category
    ) {

        if (request.getName() != null) {
            tool.setName(request.getName());
        }

        if (request.getDescription() != null) {
            tool.setDescription(request.getDescription());
        }

        if (request.getVendor() != null) {
            tool.setVendor(request.getVendor());
        }

        if (request.getWebsiteUrl() != null) {
            tool.setWebsiteUrl(request.getWebsiteUrl());
        }

        if (category != null) {
            tool.setCategory(category);
        }

        if (request.getMonthlyCost() != null) {
            tool.setMonthlyCost(request.getMonthlyCost());
        }

        if (request.getOwnerDepartment() != null) {
            tool.setOwnerDepartment(request.getOwnerDepartment());
        }

        if (request.getStatus() != null) {
            tool.setStatus(request.getStatus());
        }

        if (request.getActiveUsersCount() != null) {
            tool.setActiveUsersCount(request.getActiveUsersCount());
        }
    }
}