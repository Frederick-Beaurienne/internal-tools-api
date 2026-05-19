package com.techcorp.internaltoolsapi.shared.numeric;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Shared service responsible
 * for numeric calculations
 * and rounding rules.
 */
@Service
public class NumericService {

    // ---------- PUBLIC METHODS ---------- //

    /**
     * Applies standard financial
     * rounding to monetary values.
     *
     * Monetary values are rounded
     * to two decimal places.
     *
     * @param value monetary value
     * @return rounded monetary value
     */
    public BigDecimal roundMoney(
            BigDecimal value
    ) {

        if (value == null) {
            return BigDecimal.ZERO;
        }

        return value.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }

    /**
     * Applies percentage rounding.
     *
     * Percentages are rounded
     * to one decimal place.
     *
     * @param value percentage value
     * @return rounded percentage
     */
    public Double roundPercentage(
            BigDecimal value
    ) {

        if (value == null) {
            return 0.0;
        }

        return value.setScale(
                1,
                RoundingMode.HALF_UP
        ).doubleValue();
    }
}