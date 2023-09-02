package com.github.bluekey.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Deprecated
public class SentryCheckController {

    @GetMapping("/sentry-exception-test")
    public String healthCheck() throws Exception {
        throw new Exception("sentry health check validation.");
    }
}
