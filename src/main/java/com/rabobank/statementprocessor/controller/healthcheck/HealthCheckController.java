package com.rabobank.statementprocessor.controller.healthcheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Endpoints to check the health of the application
 */


@RestController
@RequestMapping("/keepalive")
public class HealthCheckController {

    public static final String KEEPALIVE_OK_RESPONSE = "KEEPALIVE_OK";

    @GetMapping
    public String keepalive() {
        return KEEPALIVE_OK_RESPONSE;
    }
}
