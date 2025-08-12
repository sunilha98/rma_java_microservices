package com.resourcemgmt.masterresource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcemgmt.masterresource.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	List<Project> findByStatus(String status);

	@Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'IN_FLIGHT'")
	Long countActiveProjects();

	@Query("SELECT p FROM Project p ORDER BY p.createdAt DESC")
	List<Project> findAllOrderByCreatedAtDesc();

	Project findByProjectCode(String projectCode);

	@Query("SELECT p FROM Project p WHERE p.status = 'Change Requested'")
	List<Project> findAllChangeRequests();

	@Query("SELECT DISTINCT p FROM Project p JOIN FETCH p.client JOIN FETCH p.practice")
	List<Project> findPortfolioSummary();

}