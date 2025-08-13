package com.resourcemgmt.projectsowservice.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.resourcemgmt.projectsowservice.activities.ActivityLogService;
import com.resourcemgmt.projectsowservice.dto.reports.GovernanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resourcemgmt.projectsowservice.activities.ActivityContextHolder;
import com.resourcemgmt.projectsowservice.activities.LogActivity;
import com.resourcemgmt.projectsowservice.dto.SowUploadRequest;
import com.resourcemgmt.projectsowservice.service.SowService;

@RestController
@RequestMapping("/sows")
public class SowController {

	@Autowired
	private SowService sowService;
	
	@Autowired
	private RestTemplate restTemplate;

	@PostMapping("/upload")
	@LogActivity(action = "Created SOW", module = "SOW Management")
	public ResponseEntity<String> uploadSow(@RequestParam("file") MultipartFile file,
			@RequestParam("priority") String priority, @RequestParam("clientName") String clientName,
			@RequestParam("projectName") String projectName, @RequestParam("positions") String positionsJson, @RequestHeader("X-Bearer-Token") String token)
			throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		List<SowUploadRequest.PositionRequest> positions = Arrays
				.asList(mapper.readValue(positionsJson, SowUploadRequest.PositionRequest[].class));

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);
		ActivityLogService.TOKEN = token;

		String url = "http://localhost:8080/api/clients/getByName/"+clientName;
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Long clientId= Long.parseLong(response.getBody().get("id").toString());

		SowUploadRequest request = new SowUploadRequest();
		request.setPriority(priority);
		request.setClientId(clientId);
		request.setProjectName(projectName);
		request.setPositions(positions);

		sowService.handleSowUpload(file, request);

		ActivityContextHolder.setDetail("Client", clientName);
		ActivityContextHolder.setDetail("Project", projectName);

		return ResponseEntity.ok("SoW and Project created successfully!");
	}

	@GetMapping("/getGovernanceReport")
	public ResponseEntity<List<GovernanceDTO>> getGovernanceReport(@RequestHeader("X-Bearer-Token") String token) {

		return ResponseEntity.ok(sowService.getGovernanceReport(token));
	}
}
