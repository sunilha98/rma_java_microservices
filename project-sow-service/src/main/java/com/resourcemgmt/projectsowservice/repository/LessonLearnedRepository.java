package com.resourcemgmt.projectsowservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.resourcemgmt.projectsowservice.entity.LessonLearned;

public interface LessonLearnedRepository extends JpaRepository<LessonLearned, Long> {

	// Search by keyword in title, description, or project name
	@Query("SELECT l FROM LessonLearned l " + "WHERE LOWER(l.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "   OR LOWER(l.description) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "   OR LOWER(l.project.projectCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<LessonLearned> searchByKeyword(@Param("keyword") String keyword);

	// Get all lessons for a specific project
	List<LessonLearned> findByProject_Id(Long id);
}
