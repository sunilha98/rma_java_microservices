package com.resourcemgmt.projectsowservice.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftTimingDTO {
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
}
