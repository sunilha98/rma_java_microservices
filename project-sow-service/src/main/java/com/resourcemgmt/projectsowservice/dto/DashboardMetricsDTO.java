package com.resourcemgmt.projectsowservice.dto;

public class DashboardMetricsDTO {

	private Integer activeProjects;
	private Integer totalResources;
	private Integer utilizationRate;
	private Integer benchResources;

	// Constructors
	public DashboardMetricsDTO() {
	}

	public DashboardMetricsDTO(Integer activeProjects, Integer totalResources, Integer utilizationRate,
			Integer benchResources) {
		this.activeProjects = activeProjects;
		this.totalResources = totalResources;
		this.utilizationRate = utilizationRate;
		this.benchResources = benchResources;
	}

	// Getters and Setters
	public Integer getActiveProjects() {
		return activeProjects;
	}

	public void setActiveProjects(Integer activeProjects) {
		this.activeProjects = activeProjects;
	}

	public Integer getTotalResources() {
		return totalResources;
	}

	public void setTotalResources(Integer totalResources) {
		this.totalResources = totalResources;
	}

	public Integer getUtilizationRate() {
		return utilizationRate;
	}

	public void setUtilizationRate(Integer utilizationRate) {
		this.utilizationRate = utilizationRate;
	}

	public Integer getBenchResources() {
		return benchResources;
	}

	public void setBenchResources(Integer benchResources) {
		this.benchResources = benchResources;
	}
}