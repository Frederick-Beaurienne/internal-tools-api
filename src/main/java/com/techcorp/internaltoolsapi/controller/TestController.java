package com.techcorp.internaltoolsapi.controller;

import com.techcorp.internaltoolsapi.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-error")
    public String testError() {

        throw new ResourceNotFoundException(
                "Test resource not found"
        );
    }
}