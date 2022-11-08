package org.tf.training.circuitbreaker.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/cb")
public class CircuitBreakerController {

    private final RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8081/service";

    private static final String CIRCUIT_NAME = "serviceA";

    AtomicInteger numberOfCalls = new AtomicInteger(0);

    @Autowired
    public CircuitBreakerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    @CircuitBreaker(name = CIRCUIT_NAME, fallbackMethod = "circuitBreakerFallback")
    @Retry(name = CIRCUIT_NAME)
    //@RateLimiter(name = CIRCUIT_NAME)
    public String testController(){
        System.out.println(numberOfCalls.addAndGet(1) + " Making a request to " + BASE_URL + " at :" + LocalDateTime.now());
        return restTemplate.getForObject(BASE_URL, String.class);
    }

    private String circuitBreakerFallback(Exception e){
        System.out.println(" CircuitBreaker Fallback at :" + LocalDateTime.now());
        return "This is the fallback for circuitBreaker";
    }

    private String retryFallback(Exception e){
        System.out.println(" Retry Fallback at :" + LocalDateTime.now());
        return "This is the fallback for retry";
    }

}
