package com.resourcemgmt.masterresource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resourcemgmt.masterresource.dto.DashboardMetricsDTO;
import com.resourcemgmt.masterresource.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@GetMapping("/metrics")
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('RMT') or hasRole('PROJECT_MANAGER')")
	public ResponseEntity<DashboardMetricsDTO> getDashboardMetrics() {
		DashboardMetricsDTO metrics = dashboardService.getDashboardMetrics();
		return ResponseEntity.ok(metrics);
	}
}