package com.techcorp.internaltoolsapi.service.impl;

import com.techcorp.internaltoolsapi.dto.request.CreateToolRequest;
import com.techcorp.internaltoolsapi.dto.response.ToolResponse;
import com.techcorp.internaltoolsapi.entity.Category;
import com.techcorp.internaltoolsapi.entity.Tool;
import com.techcorp.internaltoolsapi.exception.DuplicateResourceException;
import com.techcorp.internaltoolsapi.exception.ResourceNotFoundException;
import com.techcorp.internaltoolsapi.mapper.ToolMapper;
import com.techcorp.internaltoolsapi.repository.CategoryRepository;
import com.techcorp.internaltoolsapi.repository.ToolRepository;
import com.techcorp.internaltoolsapi.service.ToolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default implementation of tool business operations.
 */
@Service
public class ToolServiceImpl
        implements ToolService {

    private final CategoryRepository categoryRepository;

    // ---------- ATTRIBUTES ---------- //

    private static final Logger logger =
            LoggerFactory.getLogger(ToolServiceImpl.class);

    private final ToolRepository toolRepository;

    // ---------- CONSTRUCTORS ---------- //

    public ToolServiceImpl(
            ToolRepository toolRepository,
            CategoryRepository categoryRepository
    ) {

        this.toolRepository = toolRepository;
        this.categoryRepository = categoryRepository;
    }

    // ---------- BUSINESS METHODS ---------- //

    @Override
    public List<ToolResponse> getAllTools() {

        logger.info("Retrieving all tools");

        List<Tool> tools =
                toolRepository.findAll();

        return tools.stream()
                .map(ToolMapper::toResponse)
                .toList();
    }

    @Override
    public ToolResponse getToolById(
            Integer id
    ) {

        logger.info(
                "Retrieving tool with id: {}",
                id
        );

        Tool tool = toolRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tool with ID "
                                        + id
                                        + " does not exist"
                        )
                );

        return ToolMapper.toResponse(tool);
    }

    @Override
    public ToolResponse createTool(
            CreateToolRequest request
    ) {

        logger.info(
                "Creating tool with name: {}",
                request.getName()
        );

        if (toolRepository.existsByNameIgnoreCase(
                request.getName()
        )) {

            throw new DuplicateResourceException(
                    "A tool with this name already exists"
            );
        }

        Category category =
                categoryRepository.findById(
                                request.getCategoryId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category with ID "
                                                + request.getCategoryId()
                                                + " does not exist"
                                )
                        );

        Tool tool =
                ToolMapper.toEntity(
                        request,
                        category
                );

        Tool savedTool =
                toolRepository.save(tool);

        logger.info(
                "Tool created successfully with id: {}",
                savedTool.getId()
        );

        return ToolMapper.toResponse(
                savedTool
        );
    }
}