package com.example.aggregationservice;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/name")
public class AggregationController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${downstream.url}")
    private String downstreamUrl;

    @CircuitBreaker(name = "aggregation", fallbackMethod = "aggregateFallback")
    @PostMapping("/aggregation")
    public CompletableFuture<ResponseEntity<?>> aggregate(@RequestBody NameRequest request) {
        if (request == null || request.getName() == null) {
            throw new IllegalArgumentException("Request body must include a non-null 'name' list");
        }
        request.getName().add("Jocelyn");

        return CompletableFuture.supplyAsync(() ->
                ResponseEntity.ok(restTemplate.postForEntity(
                        downstreamUrl + "/name/aggregation",
                        request,
                        NameRequest.class
                ).getBody())
        );
    }

    private CompletableFuture<ResponseEntity<?>> aggregateFallback(NameRequest request, Exception ex) {
        return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service is temporarily unavailable. Please try again shortly.")
        );
    }
}