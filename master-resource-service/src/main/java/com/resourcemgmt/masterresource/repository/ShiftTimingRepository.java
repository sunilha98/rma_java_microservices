package com.resourcemgmt.masterresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.masterresource.entity.ShiftTiming;

public interface ShiftTimingRepository extends JpaRepository<ShiftTiming, Long> {
	boolean existsByName(String name);
}
