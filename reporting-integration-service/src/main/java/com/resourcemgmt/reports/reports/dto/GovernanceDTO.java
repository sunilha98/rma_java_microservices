package com.resourcemgmt.reports.reports.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GovernanceDTO {
	private Long sowId;
	private String projectName;
	private String approvalStatus;
	private LocalDateTime lastAuditDate;
}
