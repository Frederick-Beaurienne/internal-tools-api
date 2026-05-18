package com.techcorp.internaltoolsapi.service.impl;

import com.techcorp.internaltoolsapi.dto.request.CreateToolRequest;
import com.techcorp.internaltoolsapi.dto.request.UpdateToolRequest;
import com.techcorp.internaltoolsapi.dto.response.ToolResponse;
import com.techcorp.internaltoolsapi.entity.Category;
import com.techcorp.internaltoolsapi.entity.Tool;
import com.techcorp.internaltoolsapi.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.entity.enums.ToolStatusType;
import com.techcorp.internaltoolsapi.exception.DuplicateResourceException;
import com.techcorp.internaltoolsapi.exception.ResourceNotFoundException;
import com.techcorp.internaltoolsapi.mapper.ToolMapper;
import com.techcorp.internaltoolsapi.repository.CategoryRepository;
import com.techcorp.internaltoolsapi.repository.ToolRepository;
import com.techcorp.internaltoolsapi.service.ToolService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public List<ToolResponse> getToolsWithFilters(
            DepartmentType department,
            ToolStatusType status,
            String category,
            String vendor,
            String name,
            BigDecimal minCost,
            BigDecimal maxCost
    ) {

        logger.info(
                """
                        Retrieving tools with filters:
                        department={}, status={}, category={}, vendor={}, name={},
                        minCost={}, maxCost={}
                        """,
                department,
                status,
                category,
                vendor,
                name,
                minCost,
                maxCost
        );

        Specification<Tool> specification = (
                root,
                query,
                criteriaBuilder
        ) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (department != null) {

                predicates.add(
                        criteriaBuilder.equal(root.get("ownerDepartment"), department)
                );
            }

            if (status != null) {

                predicates.add(
                        criteriaBuilder.equal(root.get("status"), status)
                );
            }

            if (category != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("category").get("name")),
                                category.toLowerCase()
                        )
                );
            }

            if (vendor != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("vendor")),
                                vendor.toLowerCase()
                        )
                );
            }

            if (name != null) {

                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            if (minCost != null) {

                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("monthlyCost"), minCost)
                );
            }

            if (maxCost != null) {

                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("monthlyCost"), maxCost)
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };

        List<Tool> tools = toolRepository.findAll(specification);

        logger.info(
                "Retrieved {} filtered tools",
                tools.size()
        );

        return tools.stream()
                .map(ToolMapper::toResponse)
                .toList();
    }

    @Override
    public ToolResponse getToolById(Integer id) {

        logger.info(
                "Retrieving tool with id: {}",
                id
        );

        Tool tool = toolRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tool with ID " + id + " does not exist")
                );

        return ToolMapper.toResponse(tool);
    }

    @Transactional
    @Override
    public ToolResponse createTool(
            CreateToolRequest request
    ) {

        logger.info("Creating tool with name: {}", request.getName());

        if (toolRepository.existsByNameIgnoreCase(request.getName())) {

            throw new DuplicateResourceException("A tool with this name already exists");
        }

        Category category =
                categoryRepository.findById(
                                request.getCategoryId()
                        )
                        .orElseThrow(() -> new ResourceNotFoundException(
                                        "Category with ID "
                                                + request.getCategoryId()
                                                + " does not exist"
                                )
                        );

        Tool tool = ToolMapper.toEntity(request, category);

        Tool savedTool = toolRepository.save(tool);

        logger.info("Tool created successfully with id: {}", savedTool.getId());

        return ToolMapper.toResponse(savedTool);
    }

    @Transactional
    @Override
    public ToolResponse updateTool(
            Integer id,
            UpdateToolRequest request
    ) {

        logger.info("Updating tool with id: {}", id);

        Tool tool = toolRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tool with ID " + id + " does not exist"
                        )
                );

        Category category = null;

        if (request.getCategoryId() != null) {

            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Category with ID "
                                            + request.getCategoryId()
                                            + " does not exist"
                            )
                    );
        }

        if (request.getName() != null) {

            boolean nameAlreadyExists = toolRepository.existsByNameIgnoreCase(request.getName());

            if (nameAlreadyExists
                    && !tool.getName().equalsIgnoreCase(request.getName())) {

                throw new DuplicateResourceException("A tool with this name already exists");
            }
        }

        ToolMapper.updateEntity(tool, request, category);
        Tool updatedTool = toolRepository.save(tool);

        logger.info(
                "Tool updated successfully with id: {}",
                updatedTool.getId()
        );

        return ToolMapper.toResponse(updatedTool);
    }

    @Transactional
    @Override
    public void deleteTool(Integer id) {

        logger.info("Deleting tool with id: {}", id);

        Tool tool = toolRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tool with ID " + id + " does not exist"
                        )
                );

        toolRepository.delete(tool);

        logger.info("Tool deleted successfully with id: {}", id);
    }
}