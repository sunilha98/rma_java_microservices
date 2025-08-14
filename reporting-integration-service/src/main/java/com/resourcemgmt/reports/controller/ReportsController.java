package com.resourcemgmt.reports.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resourcemgmt.reports.reports.dto.BenchResourceDTO;
import com.resourcemgmt.reports.reports.dto.FinancialMetricDTO;
import com.resourcemgmt.reports.reports.dto.ForecastingDTO;
import com.resourcemgmt.reports.reports.dto.GovernanceDTO;
import com.resourcemgmt.reports.reports.dto.LessonLearnedDTO;
import com.resourcemgmt.reports.reports.dto.PortfolioDTO;
import com.resourcemgmt.reports.reports.dto.ProjectReportDTO;
import com.resourcemgmt.reports.reports.dto.ResourceAllocationDTO;
import com.resourcemgmt.reports.reports.dto.RiskIssueDTO;
import com.resourcemgmt.reports.reports.dto.SpendTrackingDTO;
import com.resourcemgmt.reports.service.ReportsService;

@RestController
@RequestMapping("/reports")
public class ReportsController {

	@Autowired
	private ReportsService reportService;

	@GetMapping("/spend-tracking")
	public ResponseEntity<List<SpendTrackingDTO>> getSpendTrackingReport(@RequestHeader("X-Bearer-Token") String token) {
		return ResponseEntity.ok(reportService.getSpendTrackingReport(token));
	}

	@GetMapping("/in-flight")
	public ResponseEntity<List<ProjectReportDTO>> getInFlightProjects(@RequestHeader("X-Bearer-Token") String token) {
		return ResponseEntity.ok(reportService.getInFlightProjects(token));
	}

	@GetMapping("/proposed")
	public ResponseEntity<List<ProjectReportDTO>> getProposedProjects(@RequestHeader("X-Bearer-Token") String token) {
		return ResponseEntity.ok(reportService.getProposedProjects(token));
	}

	@GetMapping("/risks-issues")
	public ResponseEntity<List<RiskIssueDTO>> getRisksAndIssuesReport(@RequestHeader("X-Bearer-Token") String token) {
		return ResponseEntity.ok(reportService.getRisksAndIssuesReport(token));
	}

	@GetMapping("/resource-allocation")
	public ResponseEntity<List<ResourceAllocationDTO>> getResourceAllocationReport(@RequestHeader("X-Bearer-Token") String token) {
		return ResponseEntity.ok(reportService.getResourceAllocationReport(token));
	}

	@GetMapping("/bench-tracking")
	public ResponseEntity<List<BenchResourceDTO>> getBenchTrackingReport(@RequestHeader("X-Bearer-Token") String token) {
		return ResponseEntity.ok(reportService.getBenchTrackingReport(token));
	}

	@GetMapping("/forecasting")
	public ResponseEntity<List<ForecastingDTO>> getForecastingReport(@RequestHeader("X-Bearer-Token") String token) {
		return ResponseEntity.ok(reportService.getForecastingReport(token));
	}

	@GetMapping("/financial-metrics")
	public ResponseEntity<List<FinancialMetricDTO>> getFinancialMetricsReport(@RequestHeader("X-Bearer-Token") String token) {
		return ResponseEntity.ok(reportService.getFinancialMetricsReport(token));
	}

	@GetMapping("/governance")
	public ResponseEntity<List<GovernanceDTO>> getGovernanceReport(@RequestHeader("X-Bearer-Token") String token) {
		return ResponseEntity.ok(reportService.getGovernanceReport(token));
	}

	@GetMapping("/portfolio")
	public ResponseEntity<List<PortfolioDTO>> getPortfolioDashboard(@RequestHeader("X-Bearer-Token") String token) {
		return ResponseEntity.ok(reportService.getPortfolioDashboard(token));
	}

	@GetMapping("/lessons-learned")
	public ResponseEntity<List<LessonLearnedDTO>> getLessonsLearnedRepository(@RequestHeader("X-Bearer-Token") String token) {
		return ResponseEntity.ok(reportService.getLessonsLearnedRepository(token));
	}

}
