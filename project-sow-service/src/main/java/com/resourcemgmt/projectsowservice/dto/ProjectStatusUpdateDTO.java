package com.resourcemgmt.projectsowservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStatusUpdateDTO {
	private Long id;
	private Long projectId;
	private String projectCode;
	private String milestone;
	private String deliverables;
	private Integer progress;
	private String risks;
	private String issues;
	private String updatedBy;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String status;
}
