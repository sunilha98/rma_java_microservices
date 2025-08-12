package com.resourcemgmt.masterresource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resourcemgmt.masterresource.dto.DashboardMetricsDTO;
import com.resourcemgmt.masterresource.repository.ProjectRepository;
import com.resourcemgmt.masterresource.repository.ResourceRepository;

@Service
public class DashboardService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ResourceRepository resourceRepository;

	public DashboardMetricsDTO getDashboardMetrics() {
		Long activeProjects = projectRepository.countActiveProjects();
		Long totalResources = resourceRepository.countActiveResources();
		Long benchResources = resourceRepository.countBenchResources();

		Long allocatedResources = totalResources - benchResources;
		Integer utilizationRate = totalResources > 0
				? Math.round((allocatedResources.floatValue() / totalResources.floatValue()) * 100)
				: 0;

		return new DashboardMetricsDTO(activeProjects.intValue(), totalResources.intValue(), utilizationRate,
				benchResources.intValue());
	}
}