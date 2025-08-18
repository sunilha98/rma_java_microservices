package com.resourcemgmt.usermgmt.activities;

import com.resourcemgmt.usermgmt.controller.UsersController;
import com.resourcemgmt.usermgmt.dto.ActivityLogDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityLogServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ActivityLogService activityLogService;

    @BeforeEach
    void setUp() {
        UsersController.TOKEN = "test-token";
    }

    @Test
    void logActivity() {
        // Arrange
        String action = "Test Action";
        String performedBy = "testuser";
        String role = "ADMIN";
        String module = "Test Module";
        String details = "Test Details";

        // Act
        activityLogService.logActivity(action, performedBy, role, module, details);

        // Assert
        verify(restTemplate).postForEntity(
                eq("http://localhost:8080/api/activity"),
                argThat((HttpEntity<ActivityLogDTO> entity) -> {
                    ActivityLogDTO log = entity.getBody();
                    HttpHeaders headers = entity.getHeaders();

                    return log != null &&
                            action.equals(log.getAction()) &&
                            performedBy.equals(log.getPerformedBy()) &&
                            role.equals(log.getRole()) &&
                            module.equals(log.getModule()) &&
                            details.equals(log.getDetails()) &&
                            log.getTimestamp() != null &&
                            MediaType.APPLICATION_JSON.equals(headers.getContentType()) &&
                            "Bearer test-token".equals(headers.getFirst(HttpHeaders.AUTHORIZATION));
                }),
                eq(Map.class)
        );
    }
}