package com.resourcemgmt.projectsowservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resourcemgmt.projectsowservice.dto.ProjectStatusUpdateDTO;
import com.resourcemgmt.projectsowservice.entity.Project;
import com.resourcemgmt.projectsowservice.entity.ProjectStatusUpdate;
import com.resourcemgmt.projectsowservice.repository.ProjectRepository;
import com.resourcemgmt.projectsowservice.repository.ProjectStatusUpdateRepository;

@Service
public class ProjectStatusUpdateService {

	@Autowired
	private ProjectStatusUpdateRepository statusRepo;

	@Autowired
	private ProjectRepository projectRepo;

	public ProjectStatusUpdateDTO createStatus(ProjectStatusUpdateDTO dto) {
		Project project = projectRepo.findById(dto.getProjectId())
				.orElseThrow(() -> new RuntimeException("Project not found"));

		project.setStatus(dto.getStatus());

		ProjectStatusUpdate status = new ProjectStatusUpdate();
		status.setProject(project);
		status.setMilestone(dto.getMilestone());
		status.setDeliverables(dto.getDeliverables());
		status.setProgress(dto.getProgress());
		status.setRisks(dto.getRisks());
		status.setIssues(dto.getIssues());
		status.setCreatedAt(LocalDateTime.now());
		status.setUpdatedAt(LocalDateTime.now());
		status.setUpdatedBy(dto.getUpdatedBy());

		ProjectStatusUpdate saved = statusRepo.save(status);

		return convertToDTO(saved);
	}

	public List<ProjectStatusUpdateDTO> getStatusByProject(Long projectId) {
		return statusRepo.findByProject_Id(projectId).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private ProjectStatusUpdateDTO convertToDTO(ProjectStatusUpdate status) {
		ProjectStatusUpdateDTO dto = new ProjectStatusUpdateDTO();
		dto.setId(status.getId());
		dto.setProjectId(status.getProject().getId());
		dto.setProjectCode(status.getProject().getProjectCode());
		dto.setMilestone(status.getMilestone());
		dto.setDeliverables(status.getDeliverables());
		dto.setProgress(status.getProgress());
		dto.setRisks(status.getRisks());
		dto.setIssues(status.getIssues());
		dto.setUpdatedBy(status.getUpdatedBy());
		dto.setCreatedAt(status.getCreatedAt());
		dto.setUpdatedAt(status.getUpdatedAt());
		return dto;
	}

	public List<ProjectStatusUpdateDTO> getAllStatus() {
		return statusRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}
}
