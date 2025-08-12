package com.resourcemgmt.projectsowservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectsDTO {

	private Long id;
	private String projectCode;
	private String name;
	private String clientName;
	private String practice;
	private String status;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

}
