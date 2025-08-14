package com.resourcemgmt.resourceallocations.activities;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testLogActivity_WithDetails() throws Throwable {
        LogActivity logActivity = mock(LogActivity.class);
        when(logActivity.action()).thenReturn("CREATE");
        when(logActivity.module()).thenReturn("RESOURCE");
        when(joinPoint.proceed()).thenReturn("result");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");

        ActivityContextHolder.setDetail("resourceId", "123");
        ActivityContextHolder.setDetail("action", "create");

        activityLogAspect.logActivity(joinPoint, logActivity);

        verify(activityLogService).logActivity(
                "CREATE",
                "testuser",
                "ROLE_USER",
                "RESOURCE",
                "resourceId: 123, action: create"
        );
        assertNull(ActivityContextHolder.getDetail("resourceId")); // Verify clear was called
    }

    @Test
    void testLogActivity_WithoutDetails() throws Throwable {
        LogActivity logActivity = mock(LogActivity.class);
        when(logActivity.action()).thenReturn("VIEW");
        when(logActivity.module()).thenReturn("DASHBOARD");
        when(joinPoint.proceed()).thenReturn("result");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");

        activityLogAspect.logActivity(joinPoint, logActivity);

        verify(activityLogService).logActivity(
                "VIEW",
                "testuser",
                "ROLE_USER",
                "DASHBOARD",
                "Auto-logged via AOP"
        );
    }
}