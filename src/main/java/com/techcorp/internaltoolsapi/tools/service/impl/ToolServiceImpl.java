package com.techcorp.internaltoolsapi.tools.service.impl;

import com.techcorp.internaltoolsapi.tools.dto.request.CreateToolRequest;
import com.techcorp.internaltoolsapi.tools.dto.request.UpdateToolRequest;
import com.techcorp.internaltoolsapi.tools.dto.response.PaginatedToolResponse;
import com.techcorp.internaltoolsapi.tools.dto.response.ToolDetailsResponse;
import com.techcorp.internaltoolsapi.tools.dto.response.ToolResponse;
import com.techcorp.internaltoolsapi.tools.dto.response.metadata.PaginationMetadata;
import com.techcorp.internaltoolsapi.tools.dto.response.metadata.SortingMetadata;
import com.techcorp.internaltoolsapi.tools.entity.Category;
import com.techcorp.internaltoolsapi.tools.entity.Tool;
import com.techcorp.internaltoolsapi.tools.entity.enums.DepartmentType;
import com.techcorp.internaltoolsapi.tools.entity.enums.ToolStatusType;
import com.techcorp.internaltoolsapi.exception.DuplicateResourceException;
import com.techcorp.internaltoolsapi.exception.ResourceNotFoundException;
import com.techcorp.internaltoolsapi.tools.mapper.ToolMapper;
import com.techcorp.internaltoolsapi.tools.repository.CategoryRepository;
import com.techcorp.internaltoolsapi.tools.repository.ToolRepository;
import com.techcorp.internaltoolsapi.tools.service.ToolService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public PaginatedToolResponse getToolsWithFilters(
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

        Pageable pageable = buildPageable(page, limit, sort, direction);

        Specification<Tool> specification =
                buildSpecification(
                        department,
                        status,
                        category,
                        vendor,
                        name,
                        minCost,
                        maxCost
                );

        Page<Tool> toolsPage = toolRepository.findAll(specification, pageable);

        logger.info(
                """
                        Retrieved filtered tools:
                        totalElements={}, currentPage={}, pageSize={}
                        """,
                toolsPage.getTotalElements(),
                toolsPage.getNumber(),
                toolsPage.getSize()
        );

        Map<String, Object> filtersApplied =
                buildAppliedFilters(
                        department,
                        status,
                        category,
                        vendor,
                        name,
                        minCost,
                        maxCost
                );

        return buildPaginatedResponse(
                toolsPage,
                filtersApplied,
                sort,
                direction
        );
    }

    @Override
    public ToolDetailsResponse getToolById(Integer id) {

        logger.info(
                "Retrieving tool with id: {}",
                id
        );

        Tool tool = toolRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tool with ID " + id + " does not exist")
                );

        return ToolMapper.toDetailsResponse(tool);
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

    // ---------- HELPERS ---------- //

    /**
     * Builds the final paginated API response payload.
     * <p>
     * The response contains:
     * - mapped tool DTOs
     * - pagination metadata
     * - sorting metadata
     * - applied filters metadata
     *
     * @param toolsPage       paginated tool query result
     * @param filtersApplied  applied filters metadata
     * @param sort            requested sorting field
     * @param direction       requested sorting direction
     * @return fully assembled paginated response
     */
    private PaginatedToolResponse buildPaginatedResponse(
            Page<Tool> toolsPage,
            Map<String, Object> filtersApplied,
            String sort,
            String direction
    ) {

        List<ToolResponse> toolResponses =
                toolsPage.getContent()
                        .stream()
                        .map(ToolMapper::toResponse)
                        .toList();

        PaginationMetadata pagination =
                new PaginationMetadata(
                        toolsPage.getNumber(),
                        toolsPage.getSize(),
                        toolsPage.getTotalPages(),
                        toolsPage.isFirst(),
                        toolsPage.isLast()
                );

        SortingMetadata sorting =
                new SortingMetadata(
                        sort,
                        direction
                );

        return new PaginatedToolResponse(
                toolResponses,
                toolsPage.getTotalElements(),
                toolsPage.getTotalElements(),
                filtersApplied,
                pagination,
                sorting
        );
    }

    /**
     * Builds pageable configuration including:
     * - page index
     * - page size
     * - sorting field
     * - sorting direction
     * <p>
     * Sorting fields are validated against
     * a controlled whitelist in order to:
     * - avoid invalid property access
     * - preserve API contract stability
     * - prevent unsupported sorting operations
     *
     * @param page       requested page index
     * @param limit      requested page size
     * @param sort       requested sorting field
     * @param direction  requested sorting direction
     * @return validated pageable configuration
     */
    private Pageable buildPageable(
            int page,
            int limit,
            String sort,
            String direction
    ) {

        List<String> allowedSortFields = List.of(
                "name",
                "vendor",
                "status",
                "monthlyCost",
                "activeUsersCount",
                "createdAt",
                "updatedAt"
        );

        String validatedSort =
                allowedSortFields.contains(sort)
                        ? sort
                        : "createdAt";

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        return PageRequest.of(
                page,
                limit,
                Sort.by(sortDirection, validatedSort)
        );
    }

    /**
     * Builds dynamic JPA filtering specification
     * using optional filtering criteria.
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
     *
     * @param department optional department filter
     * @param status     optional status filter
     * @param category   optional category filter
     * @param vendor     optional vendor filter
     * @param name       optional partial name filter
     * @param minCost    optional minimum monthly cost
     * @param maxCost    optional maximum monthly cost
     * @return dynamically constructed specification
     */
    private Specification<Tool> buildSpecification(
            DepartmentType department,
            ToolStatusType status,
            String category,
            String vendor,
            String name,
            BigDecimal minCost,
            BigDecimal maxCost
    ) {

        return (
                root,
                query,
                criteriaBuilder
        ) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            if (department != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("ownerDepartment"),
                                department
                        )
                );
            }

            if (status != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("status"),
                                status
                        )
                );
            }

            if (category != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(
                                        root.get("category")
                                                .get("name")
                                ),
                                category.toLowerCase()
                        )
                );
            }

            if (vendor != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(
                                        root.get("vendor")
                                ),
                                vendor.toLowerCase()
                        )
                );
            }

            if (name != null) {

                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("name")
                                ),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            if (minCost != null) {

                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("monthlyCost"),
                                minCost
                        )
                );
            }

            if (maxCost != null) {

                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("monthlyCost"),
                                maxCost
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(
                            new Predicate[0]
                    )
            );
        };
    }

    /**
     * Builds API metadata describing
     * which filters were applied to the request.
     * <p>
     * Only non-null filters are included
     * in the final metadata payload.
     *
     * @param department optional department filter
     * @param status     optional status filter
     * @param category   optional category filter
     * @param vendor     optional vendor filter
     * @param name       optional partial name filter
     * @param minCost    optional minimum monthly cost
     * @param maxCost    optional maximum monthly cost
     * @return applied filters metadata map
     */
    private Map<String, Object> buildAppliedFilters(
            DepartmentType department,
            ToolStatusType status,
            String category,
            String vendor,
            String name,
            BigDecimal minCost,
            BigDecimal maxCost
    ) {

        Map<String, Object> filtersApplied =
                new LinkedHashMap<>();

        if (department != null) {
            filtersApplied.put("department", department);
        }

        if (status != null) {
            filtersApplied.put("status", status);
        }

        if (category != null) {
            filtersApplied.put("category", category);
        }

        if (vendor != null) {
            filtersApplied.put("vendor", vendor);
        }

        if (name != null) {
            filtersApplied.put("name", name);
        }

        if (minCost != null) {
            filtersApplied.put("min_cost", minCost);
        }

        if (maxCost != null) {
            filtersApplied.put("max_cost", maxCost);
        }

        return filtersApplied;
    }
}