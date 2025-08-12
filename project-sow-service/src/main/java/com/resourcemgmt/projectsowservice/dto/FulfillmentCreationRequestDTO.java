package com.resourcemgmt.projectsowservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FulfillmentCreationRequestDTO {
	private String projectCode;
	private Long titleId;
	private List<Long> skillsetIds;
	private Long locationId;
	private Long shiftId;
	private BigDecimal experience;
	private int positions;
	private LocalDate expectedClosure;
	private String notes;

}
