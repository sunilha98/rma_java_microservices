package com.resourcemgmt.reports.feignclients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "allocations-service",
        url = "http://localhost:8080/api/allocations"
)
public interface AllocationsClient {

    @GetMapping("/getResourceAllocationReport")
    @CircuitBreaker(name = "allocations-service", fallbackMethod = "getAllocationsServiceFallback")
    ResponseEntity<List> getAllocationsReports(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("/getBenchTrackingReport")
    @CircuitBreaker(name = "allocations-service", fallbackMethod = "getAllocationsServiceFallback")
    ResponseEntity<List> getBenchTrackingReports(@RequestHeader("Authorization") String bearerToken);


    default ResponseEntity<List> getAllocationsServiceFallback(String bearerToken, Throwable throwable) {
        System.out.println("Fallback triggered: Allocation service unavailable. Reason: " + throwable.getMessage());
        return ResponseEntity.ok(List.of());
    }
}
