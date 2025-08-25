package com.resourcemgmt.reports.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialMetricDTO {
    private Long projectId;
    private String projectName;
    private BigDecimal budget;
    private BigDecimal cost;
}
