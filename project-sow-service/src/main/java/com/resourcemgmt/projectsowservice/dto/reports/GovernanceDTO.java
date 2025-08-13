package com.resourcemgmt.projectsowservice.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GovernanceDTO {
	private Long sowId;
	private String projectName;
	private String approvalStatus;
	private LocalDateTime lastAuditDate;
}
