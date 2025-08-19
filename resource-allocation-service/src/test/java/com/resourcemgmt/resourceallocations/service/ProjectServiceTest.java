package com.resourcemgmt.resourceallocations.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProjectService projectService;

    private static final String TOKEN = "test-token";
    private static final Long PROJECT_ID = 1L;

    @BeforeEach
    void setUp() {
        // Setup is handled by MockitoExtension
    }

    @Test
    void getProjectDetails_Success() {
        // Arrange
        Map<String, Object> mockResponse = Map.of(
                "id", PROJECT_ID,
                "name", "Test Project",
                "status", "ACTIVE"
        );

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(Map.class)
        )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Act
        Map<String, Object> result = projectService.getProjectDetails(PROJECT_ID, TOKEN);

        // Assert
        assertNotNull(result);
        assertEquals("Test Project", result.get("name"));
        assertEquals(PROJECT_ID, result.get("id"));
        assertEquals("ACTIVE", result.get("status"));
        verify(restTemplate, times(1)).exchange(anyString(), any(), any(), eq(Map.class));
    }

    @Test
    void getProjectDetails_Fallback_WhenServiceUnavailable() {
        // Act - Call the fallback method directly without mocking
        Map<String, Object> result = projectService.getProjectFallback(PROJECT_ID, TOKEN, new ResourceAccessException("Service unavailable"));

        // Assert
        assertNotNull(result);
        assertEquals(PROJECT_ID, result.get("id"));
        assertEquals("Project-" + PROJECT_ID, result.get("name"));
        assertEquals("UNKNOWN", result.get("status"));
        assertTrue((Boolean) result.get("fallback"));
    }



    @Test
    void getProjects_Success() {
        // Arrange
        Map<String, Object> mockResponse = Map.of(
                "projects", Map.of(
                        "1", Map.of("id", 1, "name", "Project 1"),
                        "2", Map.of("id", 2, "name", "Project 2")
                )
        );

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(Map.class)
        )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Act
        Map<String, Object> result = projectService.getProjects(TOKEN);

        // Assert
        assertNotNull(result);
        assertNotNull(result.get("projects"));
        assertFalse((Boolean) result.getOrDefault("fallback", false));
    }





    @Test
    void getProjectFallback_ReturnsExpectedStructure() {
        // Arrange
        Exception testException = new RuntimeException("Test exception");

        // Act
        Map<String, Object> result = projectService.getProjectFallback(PROJECT_ID, TOKEN, testException);

        // Assert
        assertNotNull(result);
        assertEquals(PROJECT_ID, result.get("id"));
        assertEquals("Project-" + PROJECT_ID, result.get("name"));
        assertEquals("UNKNOWN", result.get("status"));
        assertTrue((Boolean) result.get("fallback"));
    }


    @Test
    void getProjectsFallback_ReturnsExpectedStructure() {
        // Arrange
        Exception testException = new RuntimeException("Test exception");

        // Act
        Map<String, Object> result = projectService.getProjectsFallback(TOKEN, testException);

        // Assert
        assertNotNull(result);
        assertNotNull(result.get("projects"));
        assertTrue((Boolean) result.get("fallback"));
    }

    @Test
    void getProjectDetails_ThrowsException_WhenNullResponse() {
        // Arrange
        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(Map.class)
        )).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            projectService.getProjectDetails(PROJECT_ID, TOKEN);
        });
    }

    @Test
    void getProjects_ThrowsException_WhenNullResponse() {
        // Arrange
        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                eq(Map.class)
        )).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            projectService.getProjects(TOKEN);
        });
    }

}
