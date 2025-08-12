package com.resourcemgmt.reports.reports.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectReportDTO {

	private Long projectId;
	private String projectName;
	private String clientName;
	private String status;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
}
