package com.resourcemgmt.masterresource.feignclients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "resource-service",
        url = "http://localhost:8080/api/resources"
)
public interface ResourceClient {

    @GetMapping("/countActiveResources")
    @CircuitBreaker(name = "resource-service", fallbackMethod = "getResourceServiceFallback")
    ResponseEntity<Long> countActiveProjects(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("/countBenchResources")
    @CircuitBreaker(name = "resource-service", fallbackMethod = "getResourceServiceFallback")
    ResponseEntity<Long> countBenchResources(@RequestHeader("Authorization") String bearerToken);

    default ResponseEntity<Long> getResourceServiceFallback(String bearerToken, Throwable throwable) {
        System.out.println("Fallback triggered: Resource service unavailable. Reason: " + throwable.getMessage());
        return ResponseEntity.ok(0L);
    }
}
