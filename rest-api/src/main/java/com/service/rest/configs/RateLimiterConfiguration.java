package com.service.rest.configs;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfiguration {


    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        return RateLimiterRegistry.ofDefaults();
    }

    @Bean
    public RateLimiter rateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(3) // Number of allowed calls
                .limitRefreshPeriod(Duration.ofSeconds(1)) // Time period for rate limit
                .timeoutDuration(Duration.ofMillis(500)) // Timeout for rate limiting
                .build();

        return RateLimiter.of("bookServiceRateLimiter", config);
    }
}