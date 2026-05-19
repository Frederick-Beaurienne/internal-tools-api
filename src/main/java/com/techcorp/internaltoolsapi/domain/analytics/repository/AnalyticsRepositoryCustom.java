package com.techcorp.internaltoolsapi.domain.analytics.repository;

import com.techcorp.internaltoolsapi.domain.analytics.entity.enums.DepartmentCostSortField;
import com.techcorp.internaltoolsapi.domain.tools.entity.enums.ToolStatusType;
import com.techcorp.internaltoolsapi.shared.sort.SortDirection;

import java.util.List;

public interface AnalyticsRepositoryCustom {

    List<Object[]> getDepartmentCosts(
            ToolStatusType status,
            DepartmentCostSortField sortField,
            SortDirection direction
    );
}