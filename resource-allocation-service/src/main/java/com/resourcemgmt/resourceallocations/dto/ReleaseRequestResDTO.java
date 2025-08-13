package com.resourcemgmt.resourceallocations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseRequestResDTO {

    private Long id;
    private String projectName;
    private String firstName;
    private String lastName;
    private LocalDateTime effectiveDate;
    private String reason;
    private String status;
    private String notes;
    private String replacementResource;
}
