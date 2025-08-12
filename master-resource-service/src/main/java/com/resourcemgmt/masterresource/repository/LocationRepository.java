package com.resourcemgmt.masterresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resourcemgmt.masterresource.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

}
