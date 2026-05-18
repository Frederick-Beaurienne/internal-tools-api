package com.techcorp.internaltoolsapi.repository;

import com.techcorp.internaltoolsapi.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repository for tool persistence operations.
 */
public interface ToolRepository
        extends JpaRepository<Tool, Integer>,
        JpaSpecificationExecutor<Tool> {

    Optional<Tool> findByNameIgnoreCase(
            String name
    );

    boolean existsByNameIgnoreCase(
            String name
    );
}