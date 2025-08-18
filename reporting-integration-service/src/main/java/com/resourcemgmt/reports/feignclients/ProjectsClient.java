package com.resourcemgmt.reports.feignclients;

import com.resourcemgmt.reports.reports.dto.LessonLearnedDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Collections;
import java.util.List;

@FeignClient(
        name = "projects-service",
        url = "http://localhost:8080/api/projects"
)
public interface ProjectsClient {

    @GetMapping("/status/{status}")
    @CircuitBreaker(name = "projects-service", fallbackMethod = "getProjectsByStatusReportsFallback")
    ResponseEntity<List> getProjectsByStatus(@RequestHeader("Authorization") String bearerToken, @PathVariable String status);

    default ResponseEntity<List> getProjectsByStatusReportsFallback(String bearerToken, Throwable throwable) {
        System.out.println("Fallback triggered: Projects service unavailable. Reason: " + throwable.getMessage());
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/status/spend-tracking")
    @CircuitBreaker(name = "projects-service", fallbackMethod = "getSpendTrackingReportFallback")
    ResponseEntity<List> getSpendTrackingReport(String token);

    default ResponseEntity<List> getSpendTrackingReportFallback(String token, Throwable throwable) {
        System.out.println("Fallback triggered: Spend tracking report unavailable. Reason: " + throwable.getMessage());
        return ResponseEntity.ok(Collections.emptyList());
    }
}
