package com.resourcemgmt.masterresource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftTimingDTO {
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
}
