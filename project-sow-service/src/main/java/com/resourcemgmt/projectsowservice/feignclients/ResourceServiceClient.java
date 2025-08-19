package com.resourcemgmt.projectsowservice.feignclients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(
        name = "resource-service",
        url = "http://localhost:8080/api/resources"
)
public interface ResourceServiceClient {

    @GetMapping("/countByTitleName")
    @CircuitBreaker(name = "resource-service", fallbackMethod = "getResourcesServiceFallback")
    ResponseEntity<Map> countByTitleName(@RequestHeader("Authorization") String bearerToken);

    default ResponseEntity<Map> getResourcesServiceFallback(String bearerToken, Throwable throwable) {
        System.out.println("Fallback triggered: Resource service unavailable. Reason: " + throwable.getMessage());
        return ResponseEntity.ok(Map.of());
    }
}
