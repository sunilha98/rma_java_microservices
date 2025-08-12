package com.resourcemgmt.masterresource.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonLearned {

	@Id
	@GeneratedValue
	private Long id;

	private String title;
	private String description;
	private String category;
	private String createdBy;
	private LocalDateTime createdAt;
	private String updatedBy;
	private LocalDateTime updatedAt;

	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

}
