package com.resourcemgmt.reports.service;

import com.resourcemgmt.reports.feignclients.LessonsClient;
import com.resourcemgmt.reports.reports.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportsService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LessonsClient lessonsClient;

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
        return result;
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


    public List<ForecastingDTO> getForecastingReport(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = "http://localhost:8080/api/sows/getForecastingReport";
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        List<ForecastingDTO> resList = response.getBody();

        return resList;
    }

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
        String bearerToken = "Bearer " + token;
        return lessonsClient.getLessonsLearnedReports(bearerToken);
    }

}
