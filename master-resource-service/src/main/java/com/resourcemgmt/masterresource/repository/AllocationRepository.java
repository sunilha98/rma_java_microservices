package com.resourcemgmt.masterresource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resourcemgmt.masterresource.entity.Allocation;

import jakarta.transaction.Transactional;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {

	List<Allocation> findByStatus(Allocation.AllocationStatus status);

	List<Allocation> findByProjectId(Long projectId);

	List<Allocation> findByResourceId(Long resourceId);

	@Modifying
	@Transactional
	@Query("DELETE FROM Allocation a WHERE a.project.id = :projectId AND a.resource.id = :resourceId")
	void deleteByProjectIdAndResourceId(@Param("projectId") Long projectId, @Param("resourceId") Long resourceId);

	@Query("SELECT DISTINCT a.resource.id FROM Allocation a")
	List<Long> findAllAllocatedResourceIds();

	@Query("SELECT a FROM Allocation a JOIN FETCH a.resource JOIN FETCH a.project")
	List<Allocation> findAllocatedResources();

}