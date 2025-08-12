package com.resourcemgmt.projectsowservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.projectsowservice.entity.Sow;

public interface SowRepository extends JpaRepository<Sow, Long> {

}
