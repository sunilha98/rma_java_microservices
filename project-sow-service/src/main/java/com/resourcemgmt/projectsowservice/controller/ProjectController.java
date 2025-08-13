package com.resourcemgmt.projectsowservice.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.resourcemgmt.projectsowservice.activities.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.resourcemgmt.projectsowservice.activities.ActivityContextHolder;
import com.resourcemgmt.projectsowservice.activities.LogActivity;
import com.resourcemgmt.projectsowservice.dto.ProjectsDTO;
import com.resourcemgmt.projectsowservice.entity.Project;
import com.resourcemgmt.projectsowservice.repository.ProjectRepository;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping
	public ResponseEntity<List<ProjectsDTO>> getAllProjects(@RequestHeader("X-Bearer-Token") String token) {
		List<Project> projects = projectRepository.findAllOrderByCreatedAtDesc();

		ActivityLogService.TOKEN = token;
		List<ProjectsDTO> projectDTOs = projects.stream()
				.map(project -> mapToSummaryDTO(project, token))
				.collect(Collectors.toList());
		return ResponseEntity.ok(projectDTOs);
	}

	private ProjectsDTO mapToSummaryDTO(Project project, String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://localhost:8080/api/clients/"+project.getClientId();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        String clientName= response.getBody().get("name").toString();
		
		ProjectsDTO dto = new ProjectsDTO();
		dto.setId(project.getId());
		dto.setProjectCode(project.getProjectCode());
		dto.setName(project.getName());
		dto.setClientName(clientName);
		dto.setPractice(project.getPractice().getName());
		dto.setStatus(project.getStatus());
		dto.setStartDate(project.getStartDate());
		dto.setEndDate(project.getEndDate());
		return dto;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
		return projectRepository.findById(id).map(project -> ResponseEntity.ok().body(project))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@LogActivity(action = "Created Project", module = "Project Management")
	public ResponseEntity<Project> createProject(@RequestBody Project project, @RequestHeader("X-Bearer-Token") String token, @RequestHeader("X-Auth-Username") String userName) {
		project.setStatus("PROPOSED");
		Project savedProject = projectRepository.save(project);

		ActivityLogService.TOKEN = token;
		ActivityContextHolder.setDetail("Project", savedProject.getName());

		return ResponseEntity.ok(savedProject);
	}

	@PutMapping("/{id}")
	@LogActivity(action = "Updated Project", module = "Project Management")
	public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails, @RequestHeader("X-Bearer-Token") String token, @RequestHeader("X-Auth-Username") String userName) {
		ActivityLogService.TOKEN = token;
		return projectRepository.findById(id).map(project -> {
			project.setName(projectDetails.getName());
			project.setDescription(projectDetails.getDescription());
			project.setStatus(projectDetails.getStatus());
			project.setStartDate(projectDetails.getStartDate());
			project.setEndDate(projectDetails.getEndDate());
			project.setBudget(projectDetails.getBudget());
			project.setProgress(projectDetails.getProgress());
			return ResponseEntity.ok(projectRepository.save(project));
		}).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/countActiveProjects")
	public Long countActiveProjects() {
		return projectRepository.countActiveProjects();
	}
}