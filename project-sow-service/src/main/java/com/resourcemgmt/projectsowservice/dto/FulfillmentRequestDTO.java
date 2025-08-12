package com.resourcemgmt.projectsowservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class FulfillmentRequestDTO {
	private UUID id;
	private String projectName;
	private String title;
	private List<String> skills;
	private String location;
	private String status;
	private LocalDate expectedClosure;
	private String notes;
	private String shift;
	private BigDecimal experience;
	private int positions;
}
