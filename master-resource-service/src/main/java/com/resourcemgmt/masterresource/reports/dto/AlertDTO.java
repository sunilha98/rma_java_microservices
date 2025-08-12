package com.resourcemgmt.masterresource.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertDTO {
	private String alertType;
	private String message;
	private String issues;
}
