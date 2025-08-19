package com.resourcemgmt.masterresource.repository;

import com.resourcemgmt.masterresource.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

}
