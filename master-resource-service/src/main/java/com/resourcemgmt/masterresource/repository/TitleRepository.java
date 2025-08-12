package com.resourcemgmt.masterresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.masterresource.entity.Title;

public interface TitleRepository extends JpaRepository<Title, Long> {

	Title findByName(String name);
}
