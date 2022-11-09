package org.tf.training.circuitbreaker.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.tf.training.circuitbreaker.model.Response;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("/cb")
public class CircuitBreakerController {

    private static final String CIRCUIT_BREAKER_FALLBACK = "circuitBreakerFallback";
    private static final String RETRY_FALLBACK = "retryFallback";
    private static final String BASE_URL = "http://localhost:8081/service";
    private static final String CIRCUIT_NAME = "serviceA";
    private final AtomicInteger numberOfCalls = new AtomicInteger(0);

    private final RestTemplate restTemplate;

    @Autowired
    public CircuitBreakerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    @CircuitBreaker(name = CIRCUIT_NAME, fallbackMethod = CIRCUIT_BREAKER_FALLBACK)
    @Retry(name = CIRCUIT_NAME)
    //@RateLimiter(name = CIRCUIT_NAME)
    public String testController() {
        log.info(numberOfCalls.addAndGet(1) + " Making a request to " + BASE_URL + " at :" + LocalDateTime.now());
        restTemplate.getForObject(BASE_URL, String.class);
        return restTemplate.getForObject(BASE_URL, String.class);
    }

    @SuppressWarnings("unused")
    private String circuitBreakerFallback(Exception e) {
        log.info(" CircuitBreaker Fallback at :" + LocalDateTime.now());
        return "This is the fallback for circuitBreaker";
    }

    @SuppressWarnings("unused")
    private String retryFallback(Exception e) {
        log.info(" Retry Fallback at :" + LocalDateTime.now());
        return "This is the fallback for retry";
    }

}
