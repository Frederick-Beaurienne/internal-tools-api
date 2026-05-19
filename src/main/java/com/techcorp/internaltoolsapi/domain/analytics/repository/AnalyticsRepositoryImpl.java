package com.techcorp.internaltoolsapi.domain.analytics.repository;

import com.techcorp.internaltoolsapi.domain.analytics.entity.enums.DepartmentCostSortField;
import com.techcorp.internaltoolsapi.domain.tools.entity.Tool;
import com.techcorp.internaltoolsapi.domain.tools.entity.enums.ToolStatusType;
import com.techcorp.internaltoolsapi.shared.sort.SortDirection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.List;

/**
 * Criteria-based implementation
 * for analytics repository queries.
 */
public class AnalyticsRepositoryImpl
        implements AnalyticsRepositoryCustom {

    // ---------- DEPENDENCIES ---------- //

    @PersistenceContext
    private EntityManager entityManager;

    // ---------- PUBLIC METHODS ---------- //

    @Override
    public List<Object[]> getDepartmentCosts(
            ToolStatusType status,
            DepartmentCostSortField sortField,
            SortDirection direction
    ) {

        CriteriaBuilder cb =
                entityManager.getCriteriaBuilder();

        CriteriaQuery<Object[]> query =
                cb.createQuery(Object[].class);

        Root<Tool> tool =
                query.from(Tool.class);

        Expression<?> department =
                tool.get("ownerDepartment");

        Expression<?> totalCost =
                cb.sum(tool.get("monthlyCost"));

        Expression<?> toolsCount = cb.count(tool);

        Expression<?> totalUsers =
                cb.sum(tool.get("activeUsersCount"));

        Expression<?> averageCost =
                cb.avg(tool.get("monthlyCost"));

        query.multiselect(
                department,
                totalCost,
                toolsCount,
                totalUsers,
                averageCost
        );

        query.where(
                cb.equal(tool.get("status"), status)
        );

        query.groupBy(
                department
        );

        query.orderBy(
                buildSortOrder(
                        cb,
                        direction,
                        sortField,
                        department,
                        totalCost
                )
        );

        return entityManager
                .createQuery(query)
                .getResultList();
    }

    // ---------- PRIVATE METHODS ---------- //

    /**
     * Builds Criteria sort order
     * for department analytics.
     *
     * @param cb criteria builder
     * @param direction sort direction
     * @param sortField requested sort field
     * @param department department expression
     * @param totalCost total cost expression
     * @return criteria order
     */
    private Order buildSortOrder(
            CriteriaBuilder cb,
            SortDirection direction,
            DepartmentCostSortField sortField,
            Expression<?> department,
            Expression<?> totalCost
    ) {

        Expression<?> sortExpression =
                switch (sortField) {

                    case total_cost ->
                            totalCost;

                    case department ->
                            cb.function(
                                    "text",
                                    String.class,
                                    department
                            );

                    default ->
                            cb.function(
                                    "text",
                                    String.class,
                                    department
                            );
                };

        return direction == SortDirection.desc
                ? cb.desc(sortExpression)
                : cb.asc(sortExpression);
    }
}