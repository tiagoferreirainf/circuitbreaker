package org.tf.training.circuitbreaker.serviceb.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tf.training.circuitbreaker.serviceb.service.ServiceCClient;

@Slf4j
@RestController
@RequestMapping("/serviceb")
@SuppressWarnings("unused")
public class CircuitBreakerController {
    private static final String CIRCUIT_BREAKER_FALLBACK = "circuitBreakerFallbackForRandom";
    private static final String CIRCUIT_BREAKER_FALLBACK_ECHO = "circuitBreakerFallbackForEcho";
    private static final String CIRCUIT_NAME = "serviceC";

    private final ServiceCClient client;

    @Autowired
    @SuppressWarnings("unused")
    public CircuitBreakerController(ServiceCClient client){
        this.client = client;
    }

    @GetMapping("/echo")
    @CircuitBreaker(name = CIRCUIT_NAME, fallbackMethod = CIRCUIT_BREAKER_FALLBACK_ECHO)
    @SuppressWarnings("unused")
    public ResponseEntity<String> echoTest() {
        log.info("Request received at echo");
        return client.callServiceCEcho();
    }

    @GetMapping("/random/{id}")
    @CircuitBreaker(name = CIRCUIT_NAME, fallbackMethod = CIRCUIT_BREAKER_FALLBACK)
    @SuppressWarnings("unused")
    public ResponseEntity<String> randomTest(@PathVariable int id) {
        log.info("[REQUEST ID: {}] - new request", id);
        return client.callServiceCRandom(id);
    }

    @SuppressWarnings("unused")
    private ResponseEntity<String> circuitBreakerFallbackForRandom(int i, Exception e) {
        log.warn("[REQUEST ID: {}] CircuitBreaker Fallback activated with message : ({})",i, e.getMessage());
        String message = "CircuitBreaker FallBack - request  id "+i+" with message [" + e.getMessage() + "]\n";
        return new ResponseEntity<>(message, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @SuppressWarnings("unused")
    private ResponseEntity<String> circuitBreakerFallbackForEcho(Exception e) {
        log.warn("CircuitBreaker Fallback activated with message : ({})", e.getMessage());
        String message = "CircuitBreaker FallBack : [" + e.getMessage() + "]\n";
        return new ResponseEntity<>(message, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
