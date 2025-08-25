package com.resourcemgmt.masterresource.activities;

import com.resourcemgmt.masterresource.entity.ActivityLog;
import com.resourcemgmt.masterresource.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository repository;

    public static String TOKEN;

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