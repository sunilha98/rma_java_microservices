package com.resourcemgmt.resourceallocations.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "fulfillment_requests")
public class FulfillmentRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	private Long projectId;

	@ManyToOne
	@JoinColumn(name = "title_id")
	private Title title;

	@ManyToMany
	@JoinTable(name = "fulfillment_skillsets", joinColumns = @JoinColumn(name = "fulfillment_id"), inverseJoinColumns = @JoinColumn(name = "skillset_id"))
	private List<Skillset> skillsets;

	private Long locationId;

	@Enumerated(EnumType.STRING)
	private Status status;

	private LocalDate expectedClosure;

	@Column(columnDefinition = "TEXT")
	private String notes;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	private Long shiftId;

	@Column(nullable = false, precision = 3, scale = 1)
	private BigDecimal experience;

	@Column(nullable = false)
	private int positions;

	public enum Status {
		OPEN, IN_PROGRESS, FULFILLED
	}

}
