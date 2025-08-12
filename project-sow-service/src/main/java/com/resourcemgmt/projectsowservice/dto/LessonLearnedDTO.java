package com.resourcemgmt.projectsowservice.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonLearnedDTO {

	private Long id;

	@NotBlank(message = "Project ID is required")
	private String projectCode;

	@NotBlank(message = "Title is required")
	private String title;

	@NotBlank(message = "Description is required")
	private String description;

	@NotBlank(message = "Category is required")
	private String category; // e.g., Success, Failure, Risk, Recommendation

	private String createdBy;
	private LocalDateTime createdAt;
	private String updatedBy;
	private LocalDateTime updatedAt;
}
