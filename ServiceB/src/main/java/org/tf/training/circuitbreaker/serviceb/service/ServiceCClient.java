package org.tf.training.circuitbreaker.serviceb.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class ServiceCClient {

    private static final String BASE_URL = "http://localhost:8083/servicec/";
    private static final String ECHO = "echo";
    private static final String ECHO_PATH = BASE_URL + ECHO;
    private static final String RANDOM = "random";
    private static final String RANDOM_PATH = BASE_URL + RANDOM;
    private final AtomicInteger numberOfExecutedCalls = new AtomicInteger(0);
    private final RestTemplate restTemplate;

    @Autowired
    public ServiceCClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> callServiceCEcho() {
        log.info("calling echo method at {}", LocalDateTime.now());
        return restTemplate.getForEntity(ECHO_PATH, String.class);
    }

    public ResponseEntity<String> callServiceCERetry() {
        return restTemplate.getForEntity(ECHO_PATH, String.class);
    }

    public ResponseEntity<String> callServiceCRandom(int requestId) {
        log.info("[REQUEST ID: {}] RestTemplate executions: {} at time : {}", requestId, numberOfExecutedCalls.addAndGet(1), LocalDateTime.now());
        String url = RANDOM_PATH + "/" + requestId;
        return restTemplate.getForEntity(url, String.class);
    }
}
