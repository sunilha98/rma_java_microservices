package com.resourcemgmt.projectsowservice.feignclients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "client-service",
        url = "http://localhost:8080/api/clients"
)
public interface ClientsService {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "client-service", fallbackMethod = "getClientServiceFallback")
    ResponseEntity<Map> getClientByID(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id);

    default ResponseEntity<Map> getClientServiceFallback(String bearerToken, Long id, Throwable throwable) {
        System.out.println("Fallback triggered: Client service unavailable. Reason: " + throwable.getMessage());
        return ResponseEntity.ok(Map.of());
    }

    @GetMapping
    @CircuitBreaker(name = "client-service", fallbackMethod = "getAllClientsServiceFallback")
    ResponseEntity<List> getAllClients(@RequestHeader("Authorization") String bearerToken);

    default ResponseEntity<List> getAllClientsServiceFallback(String bearerToken, Throwable throwable) {
        System.out.println("Fallback triggered: Client service unavailable. Reason: " + throwable.getMessage());
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/getByName/{name}")
    @CircuitBreaker(name = "client-service", fallbackMethod = "getClientByNameServiceFallback")
    ResponseEntity<Map> getClientByName(@RequestHeader("Authorization") String bearerToken, @PathVariable String name);

    default ResponseEntity<Map> getClientByNameServiceFallback(String bearerToken, String name, Throwable throwable) {
        System.out.println("Fallback triggered: Client service unavailable. Reason: " + throwable.getMessage());
        return ResponseEntity.ok(Map.of());
    }
}
