package com.resourcemgmt.masterresource.controller;

import com.resourcemgmt.masterresource.dto.DashboardMetricsDTO;
import com.resourcemgmt.masterresource.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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