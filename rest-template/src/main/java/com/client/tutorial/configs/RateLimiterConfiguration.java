package com.client.tutorial.configs;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfiguration {

    @Bean
    public RateLimiter rateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(10) // Number of allowed calls per period
                .limitRefreshPeriod(Duration.ofSeconds(1)) // Period duration
                .timeoutDuration(Duration.ofMillis(500)) // Timeout for rate limiting
                .build();

        return RateLimiter.of("rateLimiter", config);
    }
}