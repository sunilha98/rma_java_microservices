package com.resourcemgmt.resourceallocations.activities;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Map;

import com.resourcemgmt.resourceallocations.dto.ActivityLogDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class ActivityLogServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ActivityLogService activityLogService;

    @BeforeEach
    void setUp() {
        ActivityLogService.TOKEN = "test-token";
    }

    @Test
    void testLogActivity() {
        // Mock the REST template response
        ResponseEntity<Map> mockResponse = mock(ResponseEntity.class);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(mockResponse);

        // Call the method
        activityLogService.logActivity("CREATE", "user1", "ROLE_ADMIN", "RESOURCE", "Created resource 123");

        // Verify the REST call
        verify(restTemplate).postForEntity(
                eq("http://localhost:8080/api/activity"),
                argThat((HttpEntity<ActivityLogDTO> entity) -> {
                    HttpHeaders headers = entity.getHeaders();
                    ActivityLogDTO body = entity.getBody();

                    return headers.getContentType().equals(MediaType.APPLICATION_JSON) &&
                            headers.getFirst(HttpHeaders.AUTHORIZATION).equals("Bearer test-token") &&
                            body.getAction().equals("CREATE") &&
                            body.getPerformedBy().equals("user1") &&
                            body.getRole().equals("ROLE_ADMIN") &&
                            body.getModule().equals("RESOURCE") &&
                            body.getDetails().equals("Created resource 123") &&
                            body.getTimestamp() != null;
                }),
                eq(Map.class)
        );
    }
}