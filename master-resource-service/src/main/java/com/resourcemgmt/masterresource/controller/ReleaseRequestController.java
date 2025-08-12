package com.resourcemgmt.masterresource.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resourcemgmt.masterresource.activities.ActivityContextHolder;
import com.resourcemgmt.masterresource.activities.LogActivity;
import com.resourcemgmt.masterresource.dto.ReleaseRequestDTO;
import com.resourcemgmt.masterresource.entity.ReleaseRequest;
import com.resourcemgmt.masterresource.service.ReleaseRequestService;

@RestController
@RequestMapping("/release-requests")
public class ReleaseRequestController {

	@Autowired
	private ReleaseRequestService releaseRequestService;

	@PostMapping
	@LogActivity(action = "Release Request Added", module = "Release Request Management")
	public ResponseEntity<ReleaseRequest> create(@RequestBody ReleaseRequestDTO dto) {
		ReleaseRequest saved = releaseRequestService.createReleaseRequest(dto);

		ActivityContextHolder.setDetail("Project", saved.getProject().getName());
		ActivityContextHolder.setDetail("Resource",
				saved.getResource().getFirstName() + " " + saved.getResource().getLastName());

		return ResponseEntity.ok(saved);
	}

	@GetMapping
	public ResponseEntity<List<ReleaseRequest>> getAll() {
		return ResponseEntity.ok(releaseRequestService.getAllRequests());
	}

	@PatchMapping("/{id}/status")
	@LogActivity(action = "Release Request Updated", module = "Release Request Management")
	public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
		String status = payload.get("status");
		try {
			releaseRequestService.updateStatus(id, status);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update status");
		}
	}

}
