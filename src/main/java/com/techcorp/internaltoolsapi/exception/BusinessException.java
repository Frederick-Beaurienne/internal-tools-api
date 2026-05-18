// src/main/java/com/techcorp/internaltoolsapi/exception/BusinessException.java

package com.techcorp.internaltoolsapi.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}