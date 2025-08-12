package com.resourcemgmt.masterresource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.resourcemgmt.masterresource.entity.ProjectStatusUpdate;

public interface ProjectStatusUpdateRepository extends JpaRepository<ProjectStatusUpdate, Long> {
	List<ProjectStatusUpdate> findByProject_Id(Long projectId);

	@Query("SELECT ps FROM ProjectStatusUpdate ps WHERE ps.risks IS NOT NULL")
	List<ProjectStatusUpdate> findAllWithRisks();

}
