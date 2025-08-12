package com.resourcemgmt.resourceallocations.dto;

import java.time.LocalDateTime;

public class AllocationDTO {

	private String projectCode;
	private String role;
	private Long resourceId;
	private Integer allocationPercent;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	public AllocationDTO() {
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getAllocationPercent() {
		return allocationPercent;
	}

	public void setAllocationPercent(Integer allocationPercent) {
		this.allocationPercent = allocationPercent;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

}
