package com.resourcemgmt.masterresource.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonLearnedDTO {
	private Long projectId;
	private String projectName;
	private String insight;
	private String category;
}
