package com.resourcemgmt.masterresource.feignclients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "projects-service",
        url = "http://localhost:8080/api/projects"
)
public interface ProjectsClient {

    @GetMapping("/countActiveProjects")
    @CircuitBreaker(name = "projects-service", fallbackMethod = "getProjectServiceFallback")
    ResponseEntity<Long> countActiveProjects(@RequestHeader("Authorization") String bearerToken);

    default ResponseEntity<Long> getProjectServiceFallback(String bearerToken, Throwable throwable) {
        System.out.println("Fallback triggered: Projects service unavailable. Reason: " + throwable.getMessage());
        return ResponseEntity.ok(0L);
    }

}
