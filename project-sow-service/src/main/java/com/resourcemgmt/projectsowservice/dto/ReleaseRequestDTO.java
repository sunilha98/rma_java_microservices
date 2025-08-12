package com.resourcemgmt.projectsowservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseRequestDTO {
	private Long projectId;
	private Long resourceId;
	private Long replacementId;
	private String reason;
	private LocalDateTime effectiveDate;
	private String notes;
}
