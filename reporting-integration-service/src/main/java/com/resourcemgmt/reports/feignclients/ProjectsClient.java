package com.resourcemgmt.reports.feignclients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "projects-service",
        url = "http://localhost:8080/api/projects"
)
public interface ProjectsClient {
}
