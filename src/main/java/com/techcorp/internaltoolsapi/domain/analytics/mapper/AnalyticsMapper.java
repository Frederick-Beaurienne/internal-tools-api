package com.techcorp.internaltoolsapi.domain.analytics.mapper;

import com.techcorp.internaltoolsapi.domain.analytics.dto.response.departmentcost.DepartmentCostResponse;
import com.techcorp.internaltoolsapi.domain.analytics.dto.response.expensivetools.ExpensiveToolResponse;
import com.techcorp.internaltoolsapi.domain.analytics.enums.EfficiencyRating;
import com.techcorp.internaltoolsapi.shared.numeric.NumericService;

import java.math.BigDecimal;

/**
 * Mapper responsible
 * for analytics DTO conversions.
 */
public final class AnalyticsMapper {

    private AnalyticsMapper() {
    }

    /**
     * Maps raw department cost
     * aggregation result
     * to response DTO.
     *
     * @param row            raw repository row
     * @param numericService numeric helper
     * @return mapped response
     */
    public static DepartmentCostResponse toDepartmentCostResponse(
            Object[] row,
            NumericService numericService
    ) {

        return new DepartmentCostResponse(
                row[0] != null ? row[0].toString() : null,

                numericService.roundMoney(
                        row[1] != null ? (BigDecimal) row[1] : BigDecimal.ZERO
                ),

                row[2] != null ? ((Number) row[2]).intValue() : 0,
                row[3] != null ? ((Number) row[3]).intValue() : 0,

                numericService.roundMoney(
                        row[4] != null ?
                                BigDecimal.valueOf(((Number) row[4]).doubleValue())
                                : BigDecimal.ZERO
                ),

                null
        );
    }

    /**
     * Maps raw expensive tool
     * aggregation result
     * to response DTO.
     *
     * @param row            raw repository row
     * @param costPerUser    calculated cpu
     * @param rating         efficiency rating
     * @param numericService numeric helper
     * @return mapped response
     */
    public static ExpensiveToolResponse toExpensiveToolResponse(
            Object[] row,
            BigDecimal costPerUser,
            EfficiencyRating rating,
            NumericService numericService
    ) {

        return new ExpensiveToolResponse(

                row[0] != null ? ((Number) row[0]).intValue() : null,
                row[1] != null ? row[1].toString() : null,

                numericService.roundMoney(
                        row[2] != null ? (BigDecimal) row[2] : BigDecimal.ZERO
                ),

                row[3] != null ? ((Number) row[3]).intValue() : 0,

                numericService.roundMoney(
                        costPerUser
                ),

                row[4] != null ? row[4].toString() : null,
                row[5] != null ? row[5].toString() : null,

                rating
        );
    }
}