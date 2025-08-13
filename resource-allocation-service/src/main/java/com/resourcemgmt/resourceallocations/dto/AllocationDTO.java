package com.resourcemgmt.resourceallocations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllocationDTO {

    private Long projectId;
    private String role;
    private Long resourceId;
    private Integer allocationPercent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
