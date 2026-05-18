package com.techcorp.internaltoolsapi.repository;

import com.techcorp.internaltoolsapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for category persistence operations.
 */
public interface CategoryRepository
        extends JpaRepository<Category, Integer> {
}