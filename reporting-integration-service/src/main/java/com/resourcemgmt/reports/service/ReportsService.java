package com.resourcemgmt.reports.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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


import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ReportsService {

	@Autowired
	private RestTemplate restTemplate;

	public List<ProjectReportDTO> getInFlightProjects(String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://localhost:8080/api/projects/status/IN_FLIGHT";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		return response.getBody();
	}

	public List<ProjectReportDTO> getProposedProjects(String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://localhost:8080/api/projects/status/PROPOSED";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		return response.getBody();
	}

	public List<SpendTrackingDTO> getSpendTrackingReport(String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://localhost:8080/api/projects/status/spend-tracking";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		List<Map<String, Object>> resList = response.getBody();

		List<SpendTrackingDTO> result = new ArrayList<>();
		for (Map<String, Object> resMap : resList) {
			result.add(new SpendTrackingDTO(resMap.get("clientName").toString(), resMap.get("projectName").toString(), Double.parseDouble(resMap.get("planned").toString()),
					Double.parseDouble(resMap.get("actual").toString()), Double.parseDouble(resMap.get("variance").toString())));
		}
		return  result;
	}

	public List<RiskIssueDTO> getRisksAndIssuesReport(String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://localhost:8080/api/project-status/getRisksAndIssuesReport";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		List<RiskIssueDTO> resList = response.getBody();
		return resList;
	}

	public List<ResourceAllocationDTO> getResourceAllocationReport(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://localhost:8080/api/allocations/getResourceAllocationReport";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		List<ResourceAllocationDTO> resList = response.getBody();

		return resList;
	}


	public List<BenchResourceDTO> getBenchTrackingReport(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://localhost:8080/api/allocations/getBenchTrackingReport";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		List<BenchResourceDTO> resList = response.getBody();

		return resList;
	}


//	public List<ForecastingDTO> getForecastingReport(String token) {
//		Map<String, List<Sow>> groupedSows = sowRepository.findAll().stream()
//				.collect(Collectors.groupingBy(sow -> sow.getTitle(),
//						Collectors.toList()));
//
//		List<ForecastingDTO> result = new ArrayList<>();
//
//		for (Map.Entry<String, List<Sow>> entry : groupedSows.entrySet()) {
//			String role = entry.getKey();
//			int demand = entry.getValue().size();
//
//			// Count available resources with matching title
//			int available = resourceRepository.countByTitleName(role);
//
//			result.add(new ForecastingDTO(role, "N/A", demand, available));
//		}
//
//		return result;
//	}

	public List<FinancialMetricDTO> getFinancialMetricsReport(String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://localhost:8080/api/projects/getFinancialMetricsReport";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		List<FinancialMetricDTO> resList = response.getBody();

		return resList;
	}

	public List<GovernanceDTO> getGovernanceReport(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://localhost:8080/api/sows/getGovernanceReport";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		List<GovernanceDTO> resList = response.getBody();

		return resList;
	}

	public List<PortfolioDTO> getPortfolioDashboard(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://localhost:8080/api/projects/getPortfolioReports";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		List<PortfolioDTO> resList = response.getBody();

		return resList;
	}

	public List<LessonLearnedDTO> getLessonsLearnedRepository(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://localhost:8080/api/lessons/getLessonsLearnedReports";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		List<LessonLearnedDTO> resList = response.getBody();

		return resList;
	}
}
