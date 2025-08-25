package com.resourcemgmt.reports.feignclients;

import com.resourcemgmt.reports.reports.dto.LessonLearnedDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "lessons-service",
        url = "http://localhost:8080/api/lessons"
)
public interface LessonsClient {

    @GetMapping("/getLessonsLearnedReports")
    @CircuitBreaker(name = "lessons-service", fallbackMethod = "getLessonsLearnedReportsFallback")
    List<LessonLearnedDTO> getLessonsLearnedReports(@RequestHeader("Authorization") String bearerToken);

    default List<LessonLearnedDTO> getLessonsLearnedReportsFallback(String bearerToken, Throwable throwable) {
        System.out.println("Fallback triggered: Lessons service unavailable. Reason: " + throwable.getMessage());
        return List.of();
    }
}
