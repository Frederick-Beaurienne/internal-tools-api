package com.techcorp.internaltoolsapi.exception;

import com.techcorp.internaltoolsapi.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // =========================================================
    // RESOURCE NOT FOUND
    // =========================================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException exception
    ) {

        logger.warn("Resource not found: {}", exception.getMessage());

        ApiResponse<Void> response = ApiResponse.error(
                "Resource not found",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // =========================================================
    // BUSINESS EXCEPTIONS
    // =========================================================

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException exception
    ) {

        logger.warn("Business exception: {}", exception.getMessage());

        ApiResponse<Void> response = ApiResponse.error(
                "Business validation failed",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // =========================================================
    // ANALYTICS EXCEPTIONS
    // =========================================================

    @ExceptionHandler(InvalidAnalyticsParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidAnalyticsParameterException(
            InvalidAnalyticsParameterException exception
    ) {

        logger.warn("Invalid analytics parameter: {}", exception.getMessage());

        ApiResponse<Void> response = ApiResponse.error(
                "Invalid analytics parameter",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // =========================================================
    // VALIDATION EXCEPTIONS
    // =========================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {

        Map<String, String> validationErrors = new HashMap<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {

            validationErrors.put(
                    error.getField(),
                    error.getDefaultMessage()
            );
        }

        logger.warn("Validation failed: {}", validationErrors);

        ApiResponse<Void> response =
                ApiResponse.validationError(
                        "One or more fields are invalid",
                        validationErrors
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
            ConstraintViolationException exception
    ) {

        Map<String, String> validationErrors = new HashMap<>();

        exception.getConstraintViolations()
                .forEach(violation ->
                        validationErrors.put(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                );

        logger.warn("Constraint violation: {}", validationErrors);

        ApiResponse<Void> response =
                ApiResponse.validationError(
                        "Invalid request parameters",
                        validationErrors
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // =========================================================
    // REQUEST PARAMETER EXCEPTIONS
    // =========================================================

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception
    ) {

        String message = String.format(
                "Missing required parameter '%s'",
                exception.getParameterName()
        );

        logger.warn("Missing request parameter: {}", message);

        ApiResponse<Void> response = ApiResponse.error(
                "Missing request parameter",
                message
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // =========================================================
    // JSON PARSING EXCEPTIONS
    // =========================================================

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {

        logger.warn("Malformed JSON request");

        ApiResponse<Void> response = ApiResponse.error(
                "Malformed JSON request",
                "Request body contains invalid or unreadable JSON"
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // =========================================================
    // TECHNICAL EXCEPTIONS
    // =========================================================

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<ApiResponse<Void>> handleTechnicalException(
            TechnicalException exception
    ) {

        logger.error("Technical exception occurred", exception);

        ApiResponse<Void> response = ApiResponse.error(
                "Technical error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    // =========================================================
    // GENERIC EXCEPTIONS
    // =========================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception exception
    ) {

        logger.error("Unexpected internal server error", exception);

        ApiResponse<Void> response = ApiResponse.error(
                "Internal server error",
                "An unexpected error occurred"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}