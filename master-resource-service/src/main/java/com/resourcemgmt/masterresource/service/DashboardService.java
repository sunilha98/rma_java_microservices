package com.resourcemgmt.masterresource.service;

import com.resourcemgmt.masterresource.dto.DashboardMetricsDTO;
import com.resourcemgmt.masterresource.feignclients.ProjectsClient;
import com.resourcemgmt.masterresource.feignclients.ResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DashboardService {

    private final ProjectsClient projectsClient;
    private final ResourceClient resourceClient;

    public DashboardMetricsDTO getDashboardMetrics(String token) {

//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        HttpEntity<Void> entity = new HttpEntity<>(headers);

//        String url = "http://localhost:8080/api/projects/countActiveProjects";
//        ResponseEntity<Long> response = restTemplate.exchange(url, HttpMethod.GET, entity, Long.class);
        ResponseEntity<Long> response = projectsClient.countActiveProjects("Bearer " + token);
        Long activeProjects = response.getBody();

//        String url2 = "http://localhost:8080/api/resources/countActiveResources";
//        ResponseEntity<Long> response2 = restTemplate.exchange(url2, HttpMethod.GET, entity, Long.class);
        Long totalResources = resourceClient.countActiveProjects("Bearer " + token).getBody();

//        String url3 = "http://localhost:8080/api/resources/countBenchResources";
//        ResponseEntity<Long> response3 = restTemplate.exchange(url3, HttpMethod.GET, entity, Long.class);
        Long benchResources = resourceClient.countBenchResources("Bearer " + token).getBody();

        Long allocatedResources = totalResources - benchResources;
        Integer utilizationRate = totalResources > 0
                ? Math.round((allocatedResources.floatValue() / totalResources.floatValue()) * 100)
                : 0;

        return new DashboardMetricsDTO(activeProjects.intValue(), totalResources.intValue(), utilizationRate,
                benchResources.intValue());
    }
}