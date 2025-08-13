package com.resourcemgmt.resourceallocations.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.resourcemgmt.resourceallocations.activities.ActivityLogService;
import com.resourcemgmt.resourceallocations.dto.ReleaseRequestResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.resourcemgmt.resourceallocations.activities.ActivityContextHolder;
import com.resourcemgmt.resourceallocations.activities.LogActivity;
import com.resourcemgmt.resourceallocations.dto.ReleaseRequestDTO;
import com.resourcemgmt.resourceallocations.entity.ReleaseRequest;
import com.resourcemgmt.resourceallocations.service.ReleaseRequestService;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/release-requests")
public class ReleaseRequestController {

	@Autowired
	private ReleaseRequestService releaseRequestService;

	@Autowired
	private RestTemplate restTemplate;

	@PostMapping
	@LogActivity(action = "Release Request Added", module = "Release Request Management")
	public ResponseEntity<ReleaseRequest> create(@RequestBody ReleaseRequestDTO dto, @RequestHeader("X-Bearer-Token") String token) {
		ReleaseRequest saved = releaseRequestService.createReleaseRequest(dto);

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		String url = "http://localhost:8080/api/projects/"+ saved.getProjectId();
		ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
		String projectName = response.getBody().get("name").toString();

		ActivityLogService.TOKEN = token;
		ActivityContextHolder.setDetail("Project", projectName);
		ActivityContextHolder.setDetail("Resource",
				saved.getResource().getFirstName() + " " + saved.getResource().getLastName());

		return ResponseEntity.ok(saved);
	}

	@GetMapping
	public ResponseEntity<List<ReleaseRequestResDTO>> getAll(@RequestHeader("X-Bearer-Token") String token) {
		List<ReleaseRequest> releaseRequests = releaseRequestService.getAllRequests();
		List<ReleaseRequestResDTO> releaseRequestResDTOs = new ArrayList<>();

		for (ReleaseRequest releaseRequest : releaseRequests) {

			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(token);
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			String url = "http://localhost:8080/api/projects/"+ releaseRequest.getProjectId();
			ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
			String projectName = response.getBody().get("name").toString();

			ReleaseRequestResDTO resDTO = new ReleaseRequestResDTO();
			resDTO.setId(releaseRequest.getId());
			resDTO.setFirstName(releaseRequest.getResource().getFirstName());
			resDTO.setLastName(releaseRequest.getResource().getLastName());
			resDTO.setProjectName(projectName);
			resDTO.setStatus(releaseRequest.getStatus());
			resDTO.setNotes(releaseRequest.getNotes());
			resDTO.setReason(releaseRequest.getReason());
			resDTO.setReplacementResource(releaseRequest.getReplacement().getFirstName()+" "+releaseRequest.getReplacement().getLastName());
			resDTO.setEffectiveDate(releaseRequest.getEffectiveDate());
			releaseRequestResDTOs.add(resDTO);
		}

		return ResponseEntity.ok(releaseRequestResDTOs);
	}

	@PatchMapping("/status/{id}")
	@LogActivity(action = "Release Request Updated", module = "Release Request Management")
	public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> payload, @RequestHeader("X-Bearer-Token") String token) {
		String status = payload.get("status");
		ActivityLogService.TOKEN = token;
		try {
			releaseRequestService.updateStatus(id, status);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update status");
		}
	}

}
