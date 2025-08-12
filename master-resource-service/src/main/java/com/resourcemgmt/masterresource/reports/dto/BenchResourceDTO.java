package com.resourcemgmt.masterresource.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenchResourceDTO {
	private Long resourceId;
	private String firstName;
	private String lastName;
	private String title;
	private String skills;
	private boolean isUnderutilized;
}
