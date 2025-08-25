package com.resourcemgmt.projectsowservice.activities;

import java.time.LocalDateTime;
import java.util.Map;

import com.resourcemgmt.projectsowservice.feignclients.ActivityServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.resourcemgmt.projectsowservice.dto.ActivityLogDTO;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class ActivityLogService {

	public static String TOKEN;
	private final ActivityServiceClient activityServiceClient;

	public void logActivity(String action, String performedBy, String role, String module, String details) {
		ActivityLogDTO log = new ActivityLogDTO();
		log.setAction(action);
		log.setPerformedBy(performedBy);
		log.setRole(role);
		log.setModule(module);
		log.setDetails(details);
		log.setTimestamp(LocalDateTime.now());

//		String url = "http://localhost:8080/api/activity";
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.setBearerAuth(TOKEN);
//
//		HttpEntity<ActivityLogDTO> requestEntity = new HttpEntity<>(log, headers);
//		ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

		activityServiceClient.logActivity(log, "Bearer " + TOKEN);
	}

}