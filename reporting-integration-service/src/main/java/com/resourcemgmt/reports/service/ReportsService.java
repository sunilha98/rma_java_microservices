package com.resourcemgmt.reports.service;

import com.resourcemgmt.reports.feignclients.*;
import com.resourcemgmt.reports.reports.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportsService {

    private final AllocationsClient allocationsClient;
    private final LessonsClient lessonsClient;
    private final ProjectsClient projectsClient;
    private final ProjectStatusClient projectStatusClient;
    private final SowsClient sowsClient;

    public List<ProjectReportDTO> getInFlightProjects(String token) {

        return projectsClient.getProjectsByStatus("Bearer " + token, "IN_FLIGHT").getBody();
    }

    public List<ProjectReportDTO> getProposedProjects(String token) {

        return projectsClient.getProjectsByStatus("Bearer " + token, "PROPOSED").getBody();
    }

    public List<SpendTrackingDTO> getSpendTrackingReport(String token) {

        ResponseEntity<List> response = projectsClient.getSpendTrackingReport("Bearer " + token);
        List<Map<String, Object>> resList = response.getBody();

        List<SpendTrackingDTO> result = new ArrayList<>();
        for (Map<String, Object> resMap : resList) {
            result.add(new SpendTrackingDTO(resMap.get("clientName").toString(), resMap.get("projectName").toString(), Double.parseDouble(resMap.get("planned").toString()),
                    Double.parseDouble(resMap.get("actual").toString()), Double.parseDouble(resMap.get("variance").toString())));
        }
        return result;
    }

    public List<RiskIssueDTO> getRisksAndIssuesReport(String token) {

        ResponseEntity<List<RiskIssueDTO>> response = projectStatusClient.getRisksAndIssuesReport("Bearer " + token);
        List<RiskIssueDTO> resList = response.getBody();
        return resList;
    }

    public List<ResourceAllocationDTO> getResourceAllocationReport(String token) {
        ResponseEntity<List> response = allocationsClient.getAllocationsReports("Bearer " + token);
        List<ResourceAllocationDTO> resList = response.getBody();

        return resList;
    }


    public List<BenchResourceDTO> getBenchTrackingReport(String token) {
        List<BenchResourceDTO> resList = allocationsClient.getBenchTrackingReports("Bearer " + token).getBody();

        return resList;
    }


    public List<ForecastingDTO> getForecastingReport(String token) {

        List<ForecastingDTO> resList = sowsClient.getForecastingReports("Bearer " + token).getBody();

        return resList;
    }

    public List<FinancialMetricDTO> getFinancialMetricsReport(String token) {

        List<FinancialMetricDTO> resList = projectsClient.getFinancialMetricsReport("Bearer " + token).getBody();

        return resList;
    }

    public List<GovernanceDTO> getGovernanceReport(String token) {
        List<GovernanceDTO> resList = sowsClient.getGovernanceReports("Bearer " + token).getBody();

        return resList;
    }

    public List<PortfolioDTO> getPortfolioDashboard(String token) {
        List<PortfolioDTO> resList = projectsClient.getPortfolioReports("Bearer " + token).getBody();

        return resList;
    }

    public List<LessonLearnedDTO> getLessonsLearnedRepository(String token) {
        String bearerToken = "Bearer " + token;
        return lessonsClient.getLessonsLearnedReports(bearerToken);
    }

}
