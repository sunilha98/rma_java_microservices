package com.resourcemgmt.resourceallocations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.resourceallocations.entity.Skillset;

public interface SkillSetRepository extends JpaRepository<Skillset, Long> {

}
