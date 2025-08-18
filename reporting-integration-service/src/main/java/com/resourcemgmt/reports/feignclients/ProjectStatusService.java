package com.resourcemgmt.reports.feignclients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "lessons-service",
        url = "http://localhost:8080/api/project-status"
)
public interface ProjectStatusService {
}
