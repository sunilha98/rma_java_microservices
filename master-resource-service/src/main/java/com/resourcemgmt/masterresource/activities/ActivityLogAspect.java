package com.resourcemgmt.masterresource.activities;

import java.util.stream.Collectors;

import com.resourcemgmt.masterresource.security.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.antlr.v4.runtime.Token;
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

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Around("@annotation(logActivity)")
	public Object logActivity(ProceedingJoinPoint joinPoint, LogActivity logActivity) throws Throwable {
		Object result = joinPoint.proceed();

		Claims claims = jwtTokenUtil.extractAllClaims(ActivityLogService.TOKEN);
		String username = claims.getSubject();
		String role = claims.get("role").toString();

		String details = ActivityContextHolder.getAllDetails().entrySet().stream()
				.map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining(", "));

		activityLogService.logActivity(logActivity.action(), username, role, logActivity.module(),
				details.isEmpty() ? "Auto-logged via AOP" : details);

		ActivityContextHolder.clear();

		return result;
	}
}
