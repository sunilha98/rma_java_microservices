package com.resourcemgmt.projectsowservice.feignclients;

import com.resourcemgmt.projectsowservice.dto.ActivityLogDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "activity-service", url = "http://localhost:8080/api/activity")
public interface ActivityServiceClient {

    @PostMapping
    @CircuitBreaker(name = "activity-service", fallbackMethod = "logActivityFallback")
    void logActivity(@RequestBody ActivityLogDTO log,@RequestHeader("Authorization") String bearerToken);

    default void logActivityFallback(ActivityLogDTO log, String bearerToken, Throwable throwable) {
        System.out.println("Fallback triggered: Activity service unavailable. Reason: " + throwable.getMessage());
    }
}
