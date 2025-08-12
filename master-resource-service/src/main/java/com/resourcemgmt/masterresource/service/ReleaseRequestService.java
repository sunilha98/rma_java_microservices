package com.resourcemgmt.masterresource.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resourcemgmt.masterresource.dto.ReleaseRequestDTO;
import com.resourcemgmt.masterresource.entity.Allocation;
import com.resourcemgmt.masterresource.entity.ReleaseRequest;
import com.resourcemgmt.masterresource.entity.Resource;
import com.resourcemgmt.masterresource.entity.Resource.BenchStatus;
import com.resourcemgmt.masterresource.repository.AllocationRepository;
import com.resourcemgmt.masterresource.repository.ProjectRepository;
import com.resourcemgmt.masterresource.repository.ReleaseRequestRepository;
import com.resourcemgmt.masterresource.repository.ResourceRepository;

import jakarta.transaction.Transactional;

@Service
public class ReleaseRequestService {

	@Autowired
	private ReleaseRequestRepository releaseRequestRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ResourceRepository resourceRepository;

	@Autowired
	private AllocationRepository allocationRepository;

	public ReleaseRequest createReleaseRequest(ReleaseRequestDTO dto) {
		ReleaseRequest request = new ReleaseRequest();
		request.setProject(projectRepository.findById(dto.getProjectId()).orElseThrow());
		request.setResource(resourceRepository.findById(dto.getResourceId()).orElseThrow());

		if (dto.getReplacementId() != null) {
			request.setReplacement(resourceRepository.findById(dto.getReplacementId()).orElse(null));
		}

		request.setReason(dto.getReason());
		request.setEffectiveDate(dto.getEffectiveDate());
		request.setNotes(dto.getNotes());
		request.setCreatedBy("system"); // Replace with logged-in user
		request.setCreatedAt(LocalDateTime.now());
		request.setStatus("PENDING");

		return releaseRequestRepository.save(request);
	}

	public List<ReleaseRequest> getAllRequests() {
		return releaseRequestRepository.findAll();
	}

	public void updateStatus(Long requestId, String status) {
		ReleaseRequest request = releaseRequestRepository.findById(requestId)
				.orElseThrow(() -> new RuntimeException("Release request not found"));

		request.setStatus(status);
		releaseRequestRepository.save(request);

		if ("Approved".equalsIgnoreCase(status)) {
			handleApprovedRelease(request);
		}
	}

	@Transactional
	private void handleApprovedRelease(ReleaseRequest request) {
		Long projectId = request.getProject().getId();
		Long resourceId = request.getResource().getId();

		// 1. Remove the original resource from allocation
		allocationRepository.deleteByProjectIdAndResourceId(projectId, resourceId);

		// 2. Add replacement resource if provided
		if (request.getReplacement() != null) {
			Allocation newAllocation = new Allocation();
			newAllocation.setProject(request.getProject());
			newAllocation.setResource(request.getReplacement());
			newAllocation.setStartDate(request.getEffectiveDate());
			newAllocation.setEndDate(request.getEffectiveDate().plusYears(1));
			allocationRepository.save(newAllocation);
		}

		// 3. Change bench status for both the resources
		Resource oldResource = request.getResource();
		oldResource.setBenchStatus(BenchStatus.AVAILABLE);
		resourceRepository.save(oldResource);

		Resource newResource = request.getReplacement();
		newResource.setBenchStatus(BenchStatus.ALLOCATED);
		resourceRepository.save(newResource);
	}

}
