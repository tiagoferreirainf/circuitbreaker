package org.tf.training.circuitbreaker.serviceb.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tf.training.circuitbreaker.serviceb.service.ServiceCClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@RequestMapping("/serviceb")
@SuppressWarnings("unused")
public class RetryMechanismController {
    private static final String RETRY_FALLBACK = "retryFallBack";
    private static final String CIRCUIT_NAME = "serviceC";

    private boolean isFirstRequest = true;

    private LocalDateTime storedTime = LocalDateTime.now();

    private final ServiceCClient client;

    @Autowired
    @SuppressWarnings("unused")
    public RetryMechanismController(ServiceCClient client){
        this.client = client;
    }

    @GetMapping("/retry/{id}")
    @Retry(name = CIRCUIT_NAME, fallbackMethod = RETRY_FALLBACK)
    @SuppressWarnings("unused")
    public ResponseEntity<String> echoTest(@PathVariable String id) {
        if(isFirstRequest){
            storedTime = LocalDateTime.now();
            isFirstRequest = false;
        }
        LocalDateTime currentTime = LocalDateTime.now();
        Duration between = Duration.between(storedTime, currentTime);
        log.info("Processing request id {} with time difference of {} seconds", id, between.toSeconds());
        storedTime = currentTime;
        return client.callServiceCERetry();
    }

    @SuppressWarnings("unused")
    private ResponseEntity<String> retryFallBack(int id, Exception e) {
        log.warn("RetryFallback request {} with message: ({})", id,  e.getMessage());
        String message = "RetryFallback : [" + e.getMessage() + "]\n";
        isFirstRequest = true;
        return new ResponseEntity<>(message, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
