package com.resourcemgmt.usermgmt.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "activity_logs")
public class ActivityLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String action;
	private String performedBy;
	private String role;
	private String module;

	@Column(length = 1000)
	private String details;

	private LocalDateTime timestamp;

	@PrePersist
	public void onCreate() {
		this.timestamp = LocalDateTime.now();
	}
}
