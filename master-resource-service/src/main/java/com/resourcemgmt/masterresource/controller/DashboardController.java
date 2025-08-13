package com.resourcemgmt.masterresource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.resourcemgmt.masterresource.dto.DashboardMetricsDTO;
import com.resourcemgmt.masterresource.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@GetMapping("/metrics")
	public ResponseEntity<DashboardMetricsDTO> getDashboardMetrics(@RequestHeader("X-Bearer-Token") String token) {
		DashboardMetricsDTO metrics = dashboardService.getDashboardMetrics(token);
		return ResponseEntity.ok(metrics);
	}
}