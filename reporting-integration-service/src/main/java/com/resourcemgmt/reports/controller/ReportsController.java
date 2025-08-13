package com.resourcemgmt.reports.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<List<SpendTrackingDTO>> getSpendTrackingReport() {
		return ResponseEntity.ok(reportService.getSpendTrackingReport());
	}

	@GetMapping("/in-flight")
	public ResponseEntity<List<ProjectReportDTO>> getInFlightProjects() {
		return ResponseEntity.ok(reportService.getInFlightProjects());
	}

	@GetMapping("/proposed")
	public ResponseEntity<List<ProjectReportDTO>> getProposedProjects() {
		return ResponseEntity.ok(reportService.getProposedProjects());
	}

	@GetMapping("/risks-issues")
	public ResponseEntity<List<RiskIssueDTO>> getRisksAndIssuesReport() {
		return ResponseEntity.ok(reportService.getRisksAndIssuesReport());
	}

	@GetMapping("/resource-allocation")
	public ResponseEntity<List<ResourceAllocationDTO>> getResourceAllocationReport() {
		return ResponseEntity.ok(reportService.getResourceAllocationReport());
	}

	@GetMapping("/bench-tracking")
	public ResponseEntity<List<BenchResourceDTO>> getBenchTrackingReport() {
		return ResponseEntity.ok(reportService.getBenchTrackingReport());
	}

	@GetMapping("/forecasting")
	public ResponseEntity<List<ForecastingDTO>> getForecastingReport() {
		return ResponseEntity.ok(reportService.getForecastingReport());
	}

	@GetMapping("/financial-metrics")
	public ResponseEntity<List<FinancialMetricDTO>> getFinancialMetricsReport() {
		return ResponseEntity.ok(reportService.getFinancialMetricsReport());
	}

	@GetMapping("/governance")
	public ResponseEntity<List<GovernanceDTO>> getGovernanceReport() {
		return ResponseEntity.ok(reportService.getGovernanceReport());
	}

	@GetMapping("/portfolio")
	public ResponseEntity<List<PortfolioDTO>> getPortfolioDashboard() {
		return ResponseEntity.ok(reportService.getPortfolioDashboard());
	}

	@GetMapping("/lessons-learned")
	public ResponseEntity<List<LessonLearnedDTO>> getLessonsLearnedRepository() {
		return ResponseEntity.ok(reportService.getLessonsLearnedRepository());
	}

}
