package com.resourcemgmt.reports.feignclients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "sows-service",
        url = "http://localhost:8080/api/sows"
)
public interface SowsClient {

    @GetMapping("/getForecastingReport")
    @CircuitBreaker(name = "sows-service", fallbackMethod = "getSowsServiceFallback")
    ResponseEntity<List> getForecastingReports(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("/getGovernanceReport")
    @CircuitBreaker(name = "sows-service", fallbackMethod = "getSowsServiceFallback")
    ResponseEntity<List> getGovernanceReports(@RequestHeader("Authorization") String bearerToken);

    default ResponseEntity<List> getSowsServiceFallback(String bearerToken, Throwable throwable) {
        System.out.println("Fallback triggered: SOWs service unavailable. Reason: " + throwable.getMessage());
        return ResponseEntity.ok(List.of());
    }
}
