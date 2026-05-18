// src/main/java/com/techcorp/internaltoolsapi/dto/response/ApiResponse.java

package com.techcorp.internaltoolsapi.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiResponse<T>(

        boolean success,
        String error,
        String message,
        T data,
        Map<String, String> details,
        LocalDateTime timestamp

) {

    public static <T> ApiResponse<T> success(
            T data,
            String message
    ) {

        return new ApiResponse<>(
                true,
                null,
                message,
                data,
                null,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> success(T data) {

        return success(data, "Request processed successfully");
    }

    public static <T> ApiResponse<T> error(
            String error,
            String message
    ) {

        return new ApiResponse<>(
                false,
                error,
                message,
                null,
                null,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> validationError(
            String message,
            Map<String, String> details
    ) {

        return new ApiResponse<>(
                false,
                "Validation failed",
                message,
                null,
                details,
                LocalDateTime.now()
        );
    }
}