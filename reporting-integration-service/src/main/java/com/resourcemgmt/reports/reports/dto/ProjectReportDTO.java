package com.resourcemgmt.reports.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectReportDTO {

    private Long projectId;
    private String projectName;
    private String clientName;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
