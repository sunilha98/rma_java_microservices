package com.resourcemgmt.reports.service;

import com.resourcemgmt.reports.reports.dto.ProjectReportDTO;
import com.resourcemgmt.reports.reports.dto.SpendTrackingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportsServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ReportsService reportsService;

    private String token;

    @BeforeEach
    void setUp() {
        token = "test-token";
    }

    @Test
    @DisplayName("Happy-path: fetch In-Flight Projects")
    void testGetInFlightProjects() {
        List<ProjectReportDTO> mockList = Arrays.asList(new ProjectReportDTO(), new ProjectReportDTO());
        ResponseEntity<List> response = new ResponseEntity<>(mockList, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(List.class)))
                .thenReturn(response);

        List<ProjectReportDTO> result = reportsService.getInFlightProjects(token);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Happy-path: fetch Spend Tracking Report and map correctly")
    void testGetSpendTrackingReport() {
        Map<String, Object> map1 = Map.of(
                "clientName", "ClientA",
                "projectName", "ProjectX",
                "planned", 1000.0,
                "actual", 900.0,
                "variance", 100.0
        );
        List<Map<String, Object>> mockResponse = Collections.singletonList(map1);
        ResponseEntity<List> response = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(contains("/spend-tracking"), eq(HttpMethod.GET), any(HttpEntity.class), eq(List.class)))
                .thenReturn(response);

        List<SpendTrackingDTO> result = reportsService.getSpendTrackingReport(token);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getClientName()).isEqualTo("ClientA");
        assertThat(result.get(0).getVariance()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Bearer token should be passed in headers")
    void testBearerTokenHeader() {
        List<ProjectReportDTO> mockList = Collections.singletonList(new ProjectReportDTO());
        ResponseEntity<List> response = new ResponseEntity<>(mockList, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(List.class)))
                .thenAnswer(invocation -> {
                    HttpEntity<?> entity = invocation.getArgument(2);
                    HttpHeaders headers = (HttpHeaders) entity.getHeaders();
                    assertThat(headers.getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer " + token);
                    return response;
                });

        reportsService.getInFlightProjects(token);
    }

    @Test
    @DisplayName("Error scenario: RestTemplate server error")
    void testRestTemplateServerError() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(List.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThatThrownBy(() -> reportsService.getInFlightProjects(token))
                .isInstanceOf(HttpServerErrorException.class);
    }
}
