package com.resourcemgmt.resourceallocations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resourcemgmt.resourceallocations.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

}
