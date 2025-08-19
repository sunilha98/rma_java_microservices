package com.resourcemgmt.resourceallocations.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ProjectService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String PROJECT_SERVICE_CB = "project-service";
    private static final String PROJECT_SERVICE_URL = "http://localhost:8080/api/projects/";

    @CircuitBreaker(name = PROJECT_SERVICE_CB, fallbackMethod = "getProjectFallback")
    @Retry(name = PROJECT_SERVICE_CB)
    public Map<String, Object> getProjectDetails(Long projectId, String token) {
        log.info("Fetching project details for projectId: {}", projectId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = PROJECT_SERVICE_URL + projectId;
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        }
        
        throw new RuntimeException("Failed to fetch project details");
    }

    public Map<String, Object> getProjectFallback(Long projectId, String token, Exception ex) {
        log.error("Circuit breaker activated for project service. ProjectId: {}, Error: {}", projectId, ex.getMessage());
        
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("id", projectId);
        fallbackResponse.put("name", "Project-" + projectId);
        fallbackResponse.put("status", "UNKNOWN");
        fallbackResponse.put("fallback", true);
        
        return fallbackResponse;
    }

    @CircuitBreaker(name = PROJECT_SERVICE_CB, fallbackMethod = "getProjectsFallback")
    @Retry(name = PROJECT_SERVICE_CB)
    public Map<String, Object> getProjects(String token) {
        log.info("Fetching all projects");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = PROJECT_SERVICE_URL;
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        }
        
        throw new RuntimeException("Failed to fetch projects");
    }

    public Map<String, Object> getProjectsFallback(String token, Exception ex) {
        log.error("Circuit breaker activated for projects service. Error: {}", ex.getMessage());
        
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("projects", new HashMap<>());
        fallbackResponse.put("fallback", true);
        
        return fallbackResponse;
    }
}
