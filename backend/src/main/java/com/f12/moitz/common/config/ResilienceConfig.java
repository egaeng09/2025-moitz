package com.f12.moitz.common.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ResilienceConfig {

    private final CircuitBreakerRegistry registry;

    @Bean
    public CircuitBreaker geminiBreaker() {
        final CircuitBreaker circuitBreaker = registry.circuitBreaker("gemini");
        circuitBreaker.getEventPublisher();
        return circuitBreaker;
    }

    @Bean
    public CircuitBreaker geminiRetryableBreaker() {
        final CircuitBreaker circuitBreaker = registry.circuitBreaker("geminiRetryable");
        circuitBreaker.getEventPublisher();
        return circuitBreaker;
    }

}
