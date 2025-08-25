package com.resourcemgmt.projectsowservice.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.resourcemgmt.projectsowservice.activities.ActivityLogService;
import com.resourcemgmt.projectsowservice.dto.reports.FinancialMetricDTO;
import com.resourcemgmt.projectsowservice.dto.reports.PortfolioDTO;
import com.resourcemgmt.projectsowservice.dto.reports.ProjectReportDTO;
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

		String url = "http://api-gateway:8080/api/clients/"+project.getClientId();
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

	@GetMapping("/status/{status}")
	public ResponseEntity<List<ProjectReportDTO>> getProjectByStatus(@PathVariable String status, @RequestHeader("X-Bearer-Token") String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://api-gateway:8080/api/clients";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		List<Map<String, Object>> allClients = response.getBody();

		Map<Long, String> clientMap = new HashMap<>();
		allClients.forEach(m -> {
			Long clientId = Long.valueOf(m.get("id").toString());
			String clientName = Objects.toString(m.get("name"), "");
			clientMap.put(clientId, clientName);
		});

		List<ProjectReportDTO> dtos = projectRepository
				.findByStatus(status).stream().map(p -> new ProjectReportDTO(p.getId(), p.getName(),
						clientMap.get(p.getClientId()), p.getStatus(), p.getStartDate(), p.getEndDate()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}

	@GetMapping("/spend-tracking")
	public ResponseEntity<List<Map<String, Object>>> getSpendTrackingReport(@RequestHeader("X-Bearer-Token") String token) {
		List<Project> projects = projectRepository.findByStatus("IN_FLIGHT");

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://api-gateway:8080/api/clients";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		List<Map<String, Object>> allClients = response.getBody();

		Map<Long, String> clientMap = new HashMap<>();
		allClients.forEach(m -> {
			Long clientId = Long.valueOf(m.get("id").toString());
			String clientName = Objects.toString(m.get("name"), "");
			clientMap.put(clientId, clientName);
		});

		List<Map<String, Object>> resList = new ArrayList<>();
		for (Project project : projects) {
			Map<String, Object> resMap = new HashMap<>();
			resMap.put("projectId", project.getId());
			resMap.put("clientName", clientMap.get(project.getClientId()));
			resMap.put("projectName", project.getName());
			resMap.put("status", project.getStatus());
			resMap.put("startDate", project.getStartDate());
			resMap.put("endDate", project.getEndDate());
			resMap.put("planned", project.getBudget() != null ? project.getBudget().doubleValue() : 0.0);
			resMap.put("actual", project.getActualCost() != null ? project.getActualCost().doubleValue() : 0.0);
			resMap.put("variance",
					(project.getActualCost() != null ? project.getActualCost().doubleValue() : 0.0) -
					(project.getBudget() != null ? project.getBudget().doubleValue() : 0.0));
			resList.add(resMap);
		}

		return ResponseEntity.ok(resList);
	}

	@GetMapping("/getPortfolioReports")
	public ResponseEntity<List<PortfolioDTO>> getPortfolioReports(@RequestHeader("X-Bearer-Token") String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://api-gateway:8080/api/clients";
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
		List<Map<String, Object>> allClients = response.getBody();

		Map<Long, String> clientMap = new HashMap<>();
		allClients.forEach(m -> {
			Long clientId = Long.valueOf(m.get("id").toString());
			String clientName = Objects.toString(m.get("name"), "");
			clientMap.put(clientId, clientName);
		});

		List<PortfolioDTO> results = projectRepository.findAll().stream()
				.map(p -> new PortfolioDTO(clientMap.get(p.getClientId()), p.getPractice().getName(), p.getStatus()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(results);
	}

	@GetMapping("/getFinancialMetricsReport")
	public ResponseEntity<List<FinancialMetricDTO>> getFinancialMetricsReport(@RequestHeader("X-Bearer-Token") String token) {

		List<FinancialMetricDTO> results = projectRepository.findAll().stream()
				.map(p -> new FinancialMetricDTO(p.getId(), p.getName(), p.getBudget(),
						p.getActualCost()))
				.collect(Collectors.toList());


		return ResponseEntity.ok(results);
	}
}