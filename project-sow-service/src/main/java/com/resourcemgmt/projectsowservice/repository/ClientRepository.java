package com.resourcemgmt.projectsowservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.projectsowservice.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
	Optional<Client> findByName(String name);

	boolean existsByCode(String code);
}
