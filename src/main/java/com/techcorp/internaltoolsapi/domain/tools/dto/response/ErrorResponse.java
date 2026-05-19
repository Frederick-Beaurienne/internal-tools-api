package com.techcorp.internaltoolsapi.domain.tools.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard API error response payload.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    // ---------- ATTRIBUTES ---------- //

    private String error;

    private String message;

    private Map<String, String> details;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
            timezone = "UTC"
    )
    private LocalDateTime timestamp;

    // ---------- CONSTRUCTORS ---------- //

    public ErrorResponse() {

        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(
            String error,
            String message,
            Map<String, String> details
    ) {

        this.error = error;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    // ---------- FACTORY METHODS ---------- //

    public static ErrorResponse error(
            String error,
            String message
    ) {

        return new ErrorResponse(
                error,
                message,
                null
        );
    }

    public static ErrorResponse validationError(
            String message,
            Map<String, String> details
    ) {

        return new ErrorResponse(
                "Validation failed",
                message,
                details
        );
    }

    // ---------- GETTERS & SETTERS ---------- //

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}