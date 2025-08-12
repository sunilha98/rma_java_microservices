package com.resourcemgmt.masterresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.masterresource.entity.Practice;

public interface PracticeRepository extends JpaRepository<Practice, Long> {

	Practice findByName(String name);
}
