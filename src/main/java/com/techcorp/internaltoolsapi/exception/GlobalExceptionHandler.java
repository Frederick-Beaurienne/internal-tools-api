package com.techcorp.internaltoolsapi.exception;

import com.techcorp.internaltoolsapi.tools.dto.response.ErrorResponse;
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
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global REST exception handler responsible for:
 * - business errors
 * - validation errors
 * - technical errors
 * - resource lookup failures
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------- LOGGER ---------- //

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // =========================================================
    // RESOURCE NOT FOUND
    // =========================================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception
    ) {

        logger.warn("Resource not found: {}", exception.getMessage());

        ErrorResponse response = ErrorResponse.error(
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
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException exception
    ) {

        logger.warn("Business exception: {}", exception.getMessage());

        ErrorResponse response = ErrorResponse.error(
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
    public ResponseEntity<ErrorResponse> handleInvalidAnalyticsParameterException(
            InvalidAnalyticsParameterException exception
    ) {

        logger.warn(
                "Invalid analytics parameter: {}",
                exception.getMessage()
        );

        ErrorResponse response = ErrorResponse.error(
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
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
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

        ErrorResponse response =
                ErrorResponse.validationError(
                        "One or more fields are invalid",
                        validationErrors
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
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

        ErrorResponse response =
                ErrorResponse.validationError(
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
    public ResponseEntity<ErrorResponse>
    handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception
    ) {

        String message = String.format(
                "Missing required parameter '%s'",
                exception.getParameterName()
        );

        logger.warn("Missing request parameter: {}", message);

        ErrorResponse response = ErrorResponse.error(
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
    public ResponseEntity<ErrorResponse>
    handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {

        logger.warn("Malformed JSON request");

        ErrorResponse response = ErrorResponse.error(
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
    public ResponseEntity<ErrorResponse> handleTechnicalException(
            TechnicalException exception
    ) {

        logger.error("Technical exception occurred", exception);

        ErrorResponse response = ErrorResponse.error(
                "Technical error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    // =========================================================
    // DUPLICATE RESOURCE EXCEPTIONS
    // =========================================================

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse>
    handleDuplicateResourceException(
            DuplicateResourceException exception
    ) {

        logger.warn(
                "Duplicate resource: {}",
                exception.getMessage()
        );

        ErrorResponse response =
                ErrorResponse.error(
                        "Duplicate resource",
                        exception.getMessage()
                );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    // =========================================================
    // STATIC RESOURCE / ROUTING EXCEPTIONS
    // =========================================================

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse>
    handleNoResourceFoundException(
            NoResourceFoundException exception
    ) {

        logger.warn(
                "Requested resource not found: {}",
                exception.getMessage()
        );

        ErrorResponse response = ErrorResponse.error(
                "Resource not found",
                "Requested resource does not exist"
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // =========================================================
    // GENERIC EXCEPTIONS
    // =========================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception exception
    ) {

        logger.error(
                "Unexpected internal server error",
                exception
        );

        ErrorResponse response = ErrorResponse.error(
                "Internal server error",
                "An unexpected error occurred"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}