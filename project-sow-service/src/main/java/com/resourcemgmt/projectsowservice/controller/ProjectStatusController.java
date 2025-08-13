package com.resourcemgmt.projectsowservice.controller;

import java.util.List;

import com.resourcemgmt.projectsowservice.activities.ActivityLogService;
import com.resourcemgmt.projectsowservice.dto.reports.RiskIssueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.resourcemgmt.projectsowservice.activities.ActivityContextHolder;
import com.resourcemgmt.projectsowservice.activities.LogActivity;
import com.resourcemgmt.projectsowservice.dto.ProjectStatusUpdateDTO;
import com.resourcemgmt.projectsowservice.service.ProjectStatusUpdateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/project-status")
public class ProjectStatusController {

	@Autowired
	private ProjectStatusUpdateService service;

	@PostMapping
	@LogActivity(action = "Added Project Status", module = "Project Status Management")
	public ResponseEntity<ProjectStatusUpdateDTO> createStatus(@RequestBody @Valid ProjectStatusUpdateDTO dto,
			@RequestHeader("X-Bearer-Token") String token, @RequestHeader("X-Auth-Username") String userName) {
		dto.setUpdatedBy(userName);
		ProjectStatusUpdateDTO projectStatusResDto = service.createStatus(dto);

		ActivityLogService.TOKEN = token;
		ActivityContextHolder.setDetail("Project Code", projectStatusResDto.getProjectCode());
		ActivityContextHolder.setDetail("Status", projectStatusResDto.getStatus());

		return ResponseEntity.status(HttpStatus.CREATED).body(projectStatusResDto);
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<List<ProjectStatusUpdateDTO>> getStatusByProject(@PathVariable Long projectId) {
		return ResponseEntity.ok(service.getStatusByProject(projectId));
	}

	@GetMapping
	public ResponseEntity<List<ProjectStatusUpdateDTO>> getAllStatus() {
		return ResponseEntity.ok(service.getAllStatus());
	}

	@GetMapping("/getRisksAndIssuesReport")
	public ResponseEntity<List<RiskIssueDTO>> getRisksAndIssuesReport() {
		return ResponseEntity.ok(service.getRisksAndIssuesReport());
	}
}
