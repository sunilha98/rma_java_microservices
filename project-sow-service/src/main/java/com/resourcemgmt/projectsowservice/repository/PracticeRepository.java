package com.resourcemgmt.projectsowservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.projectsowservice.entity.Practice;

public interface PracticeRepository extends JpaRepository<Practice, Long> {

	Practice findByName(String name);
}
