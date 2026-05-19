// src/main/java/com/techcorp/internaltoolsapi/exception/TechnicalException.java

package com.techcorp.internaltoolsapi.api.exception;

public class TechnicalException extends RuntimeException {

    public TechnicalException(String message) {
        super(message);
    }

    public TechnicalException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}