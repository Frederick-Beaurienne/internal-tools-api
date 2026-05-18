package com.techcorp.internaltoolsapi.mapper;

import com.techcorp.internaltoolsapi.dto.request.CreateToolRequest;
import com.techcorp.internaltoolsapi.dto.response.ToolResponse;
import com.techcorp.internaltoolsapi.entity.Category;
import com.techcorp.internaltoolsapi.entity.Tool;
import com.techcorp.internaltoolsapi.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.entity.enums.ToolStatusType;

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
        tool.setStatus(request.getStatus());
        tool.setActiveUsersCount(request.getActiveUsersCount());

        return tool;
    }
}