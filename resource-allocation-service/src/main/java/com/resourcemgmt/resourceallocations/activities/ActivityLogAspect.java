package com.resourcemgmt.resourceallocations.activities;

import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ActivityLogAspect {

	@Autowired
	private ActivityLogService activityLogService;

	@Around("@annotation(logActivity)")
	public Object logActivity(ProceedingJoinPoint joinPoint, LogActivity logActivity) throws Throwable {
		Object result = joinPoint.proceed();

		String performedBy = SecurityContextHolder.getContext().getAuthentication().getName();
		String role = "ROLE_USER"; // You can extract from JWT or SecurityContext if available

		String details = ActivityContextHolder.getAllDetails().entrySet().stream()
				.map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining(", "));

		activityLogService.logActivity(logActivity.action(), performedBy, role, logActivity.module(),
				details.isEmpty() ? "Auto-logged via AOP" : details);

		ActivityContextHolder.clear();

		return result;
	}
}
