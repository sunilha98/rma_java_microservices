package com.resourcemgmt.reports.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskIssueDTO {
    private Long riskId;
    private String projectName;
    private String risk;
    private String issue;
    private Integer progress;
}