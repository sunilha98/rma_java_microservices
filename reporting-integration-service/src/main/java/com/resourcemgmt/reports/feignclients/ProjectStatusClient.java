package com.resourcemgmt.reports.feignclients;

import com.resourcemgmt.reports.reports.dto.RiskIssueDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@FeignClient(
        name = "project-status-service",
        url = "http://localhost:8080/api/project-status"
)
public interface ProjectStatusClient {

    @GetMapping("/getRisksAndIssuesReport")
    @CircuitBreaker(name = "project-status-service", fallbackMethod = "getRiskIssueReportFallback")
    ResponseEntity<List<RiskIssueDTO>> getRisksAndIssuesReport(String token);

    default ResponseEntity<List> getRiskIssueReportFallback(String token, Throwable throwable) {
        System.out.println("Fallback method called for getRisksAndIssuesReport: " + throwable.getMessage());
        return ResponseEntity.ok(Collections.emptyList());
    }
}
