package com.techcorp.internaltoolsapi.tools.repository;

import com.techcorp.internaltoolsapi.tools.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for category persistence operations.
 */
public interface CategoryRepository
        extends JpaRepository<Category, Integer> {
}