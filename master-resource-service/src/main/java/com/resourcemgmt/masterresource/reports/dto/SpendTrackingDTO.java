package com.resourcemgmt.masterresource.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpendTrackingDTO {

	private String clientName;
	private String projectName;
	private double plannedSpend;
	private double actualSpend;
	private double variance;

}
