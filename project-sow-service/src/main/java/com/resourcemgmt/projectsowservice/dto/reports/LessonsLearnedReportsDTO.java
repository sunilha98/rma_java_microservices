package com.resourcemgmt.projectsowservice.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonsLearnedReportsDTO {
    private Long projectId;
    private String projectName;
    private String insight;
    private String category;
}