package com.resourcemgmt.resourceallocations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.resourceallocations.entity.ShiftTiming;

public interface ShiftTimingRepository extends JpaRepository<ShiftTiming, Long> {
	boolean existsByName(String name);
}
