package com.resourcemgmt.usermgmt.activities;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.resourcemgmt.usermgmt.dto.ActivityLogDTO;
import org.springframework.web.client.RestTemplate;

@Service
public class ActivityLogService {

	@Autowired
	private RestTemplate restTemplate;

	public void logActivity(String action, String performedBy, String role, String module, String details) {
		ActivityLogDTO log = new ActivityLogDTO();
		log.setAction(action);
		log.setPerformedBy(performedBy);
		log.setRole(role);
		log.setModule(module);
		log.setDetails(details);
		log.setTimestamp(LocalDateTime.now());

		String url = "http://localhost:8080/api/activity";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ActivityLogDTO> requestEntity = new HttpEntity<>(log, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
	}

}