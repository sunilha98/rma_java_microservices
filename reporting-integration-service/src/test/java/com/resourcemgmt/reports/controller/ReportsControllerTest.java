package com.resourcemgmt.reports.controller;

import com.resourcemgmt.reports.reports.dto.ProjectReportDTO;
import com.resourcemgmt.reports.reports.dto.SpendTrackingDTO;
import com.resourcemgmt.reports.service.ReportsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportsControllerTest {

    @Mock
    private ReportsService reportsService;

    @InjectMocks
    private ReportsController reportsController;

    private final String token = "test-token";

    @Test
    @DisplayName("Should get Spend Tracking report")
    void testGetSpendTrackingReport() {
        SpendTrackingDTO dto = new SpendTrackingDTO("ClientA", "ProjX", 1000.0, 900.0, 100.0);
        when(reportsService.getSpendTrackingReport(token)).thenReturn(Collections.singletonList(dto));

        ResponseEntity<List<SpendTrackingDTO>> response = reportsController.getSpendTrackingReport(token);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getClientName()).isEqualTo("ClientA");
    }

    @Test
    @DisplayName("Should get In-Flight Projects")
    void testGetInFlightProjects() {
        ProjectReportDTO dto = new ProjectReportDTO();
        when(reportsService.getInFlightProjects(token)).thenReturn(Collections.singletonList(dto));

        ResponseEntity<List<ProjectReportDTO>> response = reportsController.getInFlightProjects(token);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("Empty Spend Tracking report")
    void testEmptySpendTrackingReport() {
        when(reportsService.getSpendTrackingReport(token)).thenReturn(Collections.emptyList());

        ResponseEntity<List<SpendTrackingDTO>> response = reportsController.getSpendTrackingReport(token);
        assertThat(response.getBody()).isEmpty();
    }
}
