package com.resourcemgmt.resourceallocations.dto;

import java.time.LocalDateTime;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLogDTO {

	private Long id;

	private String action;
	private String performedBy;
	private String role;
	private String module;

	private String details;

	private LocalDateTime timestamp;

	@PrePersist
	public void onCreate() {
		this.timestamp = LocalDateTime.now();
	}
}
