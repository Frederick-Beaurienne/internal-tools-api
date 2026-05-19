// src/main/java/com/techcorp/internaltoolsapi/exception/InvalidAnalyticsParameterException.java

package com.techcorp.internaltoolsapi.api.exception;

public class InvalidAnalyticsParameterException extends RuntimeException {

    public InvalidAnalyticsParameterException(String message) {
        super(message);
    }
}