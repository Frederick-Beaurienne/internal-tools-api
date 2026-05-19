package com.techcorp.internaltoolsapi.api.exception;

import java.util.Map;

/**
 * Exception thrown when
 * analytics parameters
 * are invalid.
 */
public class InvalidAnalyticsParameterException
        extends RuntimeException {

    // ---------- ATTRIBUTES ---------- //

    private final Map<String, String> details;

    // ---------- CONSTRUCTORS ---------- //

    public InvalidAnalyticsParameterException(
            String message,
            Map<String, String> details
    ) {

        super(message);
        this.details = details;
    }

    // ---------- GETTERS ---------- //

    public Map<String, String> getDetails() {
        return details;
    }
}