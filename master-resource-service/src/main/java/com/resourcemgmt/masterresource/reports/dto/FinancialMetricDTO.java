package com.resourcemgmt.masterresource.reports.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialMetricDTO {
	private Long projectId;
	private String projectName;
	private BigDecimal budget;
	private BigDecimal cost;
}
