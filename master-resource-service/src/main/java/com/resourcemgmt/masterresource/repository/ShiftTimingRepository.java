package com.resourcemgmt.masterresource.repository;

import com.resourcemgmt.masterresource.entity.ShiftTiming;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftTimingRepository extends JpaRepository<ShiftTiming, Long> {
    boolean existsByName(String name);
}
