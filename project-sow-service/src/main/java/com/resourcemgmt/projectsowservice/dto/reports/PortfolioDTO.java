package com.resourcemgmt.projectsowservice.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDTO {
	private String clientName;
	private String practice;
	private String status;
}
