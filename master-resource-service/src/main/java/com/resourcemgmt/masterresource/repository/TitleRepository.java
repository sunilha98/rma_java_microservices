package com.resourcemgmt.masterresource.repository;

import com.resourcemgmt.masterresource.entity.Title;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRepository extends JpaRepository<Title, Long> {

    Title findByName(String name);
}
