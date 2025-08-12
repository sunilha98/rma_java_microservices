package com.resourcemgmt.reports.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastingDTO {
	private String role;
	private String skill;
	private int futureDemand;
	private int availableResources;
}
