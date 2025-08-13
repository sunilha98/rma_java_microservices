package com.resourcemgmt.projectsowservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SowUploadRequest {
	private String priority;
	private Long clientId;
	private String projectName;
	private List<PositionRequest> positions;


	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PositionRequest {
		private String title;
		private String experience;
		private String skills;
		private String location;
		private String shift;


	}
}
