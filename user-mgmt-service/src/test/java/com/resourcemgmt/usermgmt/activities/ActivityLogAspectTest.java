package com.resourcemgmt.usermgmt.activities;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityLogAspectTest {

    @Mock
    private ActivityLogService activityLogService;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ActivityLogAspect activityLogAspect;

    @Test
    void logActivity() throws Throwable {
        // Arrange
        LogActivity logActivity = mock(LogActivity.class);
        when(logActivity.action()).thenReturn("Test Action");
        when(logActivity.module()).thenReturn("Test Module");

        when(joinPoint.proceed()).thenReturn("result");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        ActivityContextHolder.setDetail("key", "value");

        // Act
        Object result = activityLogAspect.logActivity(joinPoint, logActivity);

        // Assert
        assertEquals("result", result);
        verify(activityLogService).logActivity(
                "Test Action",
                "testuser",
                "ROLE_USER",
                "Test Module",
                "key: value"
        );
    }
}