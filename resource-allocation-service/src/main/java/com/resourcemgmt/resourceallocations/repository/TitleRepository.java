package com.resourcemgmt.resourceallocations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.resourceallocations.entity.Title;

public interface TitleRepository extends JpaRepository<Title, Long> {

	Title findByName(String name);
}
