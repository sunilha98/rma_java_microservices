package com.resourcemgmt.masterresource.controller;

import com.resourcemgmt.masterresource.activities.ActivityLogService;
import com.resourcemgmt.masterresource.entity.ActivityLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityLogController {

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping("/recent")
    public ResponseEntity<List<ActivityLog>> getRecentActivities() {
        return ResponseEntity.ok(activityLogService.getRecentActivities());
    }

    @PostMapping
    public ResponseEntity<ActivityLog> logActivity(@RequestBody ActivityLog activityLog) {
        activityLogService.logActivity(activityLog.getAction(),
                activityLog.getPerformedBy(),
                activityLog.getRole(),
                activityLog.getModule(),
                activityLog.getDetails());
        return ResponseEntity.ok(activityLog);
    }
}
