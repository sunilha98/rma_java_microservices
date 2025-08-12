package com.resourcemgmt.resourceallocations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resourcemgmt.resourceallocations.entity.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

	List<Resource> findByIsActiveTrue();

	List<Resource> findByIsActiveTrueAndBenchStatus(Resource.BenchStatus benchStatus);

	@Query("SELECT COUNT(r) FROM Resource r WHERE r.isActive = true")
	Long countActiveResources();

	@Query("SELECT COUNT(r) FROM Resource r WHERE r.isActive = true AND r.benchStatus = 'AVAILABLE'")
	Long countBenchResources();

	@Query("SELECT COUNT(r) FROM Resource r JOIN r.skillsets s WHERE r.title.name = :title AND s.name = :skill")
	long countByTitleAndSkill(@Param("title") String title, @Param("skill") String skill);
	
	@Query("SELECT COUNT(r) FROM Resource r WHERE r.title.name = :title")
	int countByTitleName(@Param("title") String title);


}