// src/main/java/com/techcorp/internaltoolsapi/exception/ResourceNotFoundException.java

package com.techcorp.internaltoolsapi.api.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}