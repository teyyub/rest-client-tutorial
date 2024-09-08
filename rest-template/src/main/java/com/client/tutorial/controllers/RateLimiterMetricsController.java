package com.client.tutorial.controllers;

import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class RateLimiterMetricsController {

    @Autowired
    private RateLimiter rateLimiter;

//    @GetMapping
//    public RateLimiterMetrics getMetrics() {
//        return RateLimiterMetrics.of(rateLimiter);
//    }
}