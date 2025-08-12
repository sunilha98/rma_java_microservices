package com.resourcemgmt.masterresource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.masterresource.entity.ReleaseRequest;

public interface ReleaseRequestRepository extends JpaRepository<ReleaseRequest, Long> {
	List<ReleaseRequest> findByProjectId(Long projectId);
}
