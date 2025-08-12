package com.resourcemgmt.projectsowservice.activities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.resourcemgmt.projectsowservice.entity.ActivityLog;
import com.resourcemgmt.projectsowservice.repository.ActivityLogRepository;

@Service
public class ActivityLogService {

	@Autowired
	private ActivityLogRepository repository;

	@Async("activityExecutor")
	public void logActivity(String action, String performedBy, String role, String module, String details) {
		ActivityLog log = new ActivityLog();
		log.setAction(action);
		log.setPerformedBy(performedBy);
		log.setRole(role);
		log.setModule(module);
		log.setDetails(details);
		log.setTimestamp(LocalDateTime.now());
		repository.save(log);
	}

	public List<ActivityLog> getRecentActivities() {
		return repository.findTop10ByOrderByTimestampDesc();
	}
}