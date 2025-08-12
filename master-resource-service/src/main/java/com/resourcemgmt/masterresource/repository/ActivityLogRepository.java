package com.resourcemgmt.masterresource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.masterresource.entity.ActivityLog;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
	List<ActivityLog> findTop10ByOrderByTimestampDesc();
}
