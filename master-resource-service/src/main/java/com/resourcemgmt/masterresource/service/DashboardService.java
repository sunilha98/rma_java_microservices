package com.resourcemgmt.masterresource.service;

import com.resourcemgmt.masterresource.dto.DashboardMetricsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DashboardService {

    @Autowired
    private RestTemplate restTemplate;

    public DashboardMetricsDTO getDashboardMetrics(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = "http://api-gateway:8080/api/projects/countActiveProjects";
        ResponseEntity<Long> response = restTemplate.exchange(url, HttpMethod.GET, entity, Long.class);
        Long activeProjects = response.getBody();

        String url2 = "http://api-gateway:8080/api/resources/countActiveResources";
        ResponseEntity<Long> response2 = restTemplate.exchange(url2, HttpMethod.GET, entity, Long.class);
        Long totalResources = response2.getBody();

        String url3 = "http://api-gateway:8080/api/resources/countBenchResources";
        ResponseEntity<Long> response3 = restTemplate.exchange(url3, HttpMethod.GET, entity, Long.class);
        Long benchResources = response3.getBody();

        Long allocatedResources = totalResources - benchResources;
        Integer utilizationRate = totalResources > 0
                ? Math.round((allocatedResources.floatValue() / totalResources.floatValue()) * 100)
                : 0;

        return new DashboardMetricsDTO(activeProjects.intValue(), totalResources.intValue(), utilizationRate,
                benchResources.intValue());
    }
}