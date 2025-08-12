package com.resourcemgmt.projectsowservice.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

	@PostMapping("/upload")
	@LogActivity(action = "Created SOW", module = "SOW Management")
	public ResponseEntity<String> uploadSow(@RequestParam("file") MultipartFile file,
			@RequestParam("priority") String priority, @RequestParam("clientName") String clientName,
			@RequestParam("projectName") String projectName, @RequestParam("positions") String positionsJson)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<SowUploadRequest.PositionRequest> positions = Arrays
				.asList(mapper.readValue(positionsJson, SowUploadRequest.PositionRequest[].class));

		SowUploadRequest request = new SowUploadRequest();
		request.setPriority(priority);
		request.setClientName(clientName);
		request.setProjectName(projectName);
		request.setPositions(positions);

		sowService.handleSowUpload(file, request);

		ActivityContextHolder.setDetail("Client", clientName);
		ActivityContextHolder.setDetail("Project", projectName);

		return ResponseEntity.ok("SoW and Project created successfully!");
	}
}
