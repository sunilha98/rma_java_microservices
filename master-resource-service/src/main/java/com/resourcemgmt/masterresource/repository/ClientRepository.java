package com.resourcemgmt.masterresource.repository;

import com.resourcemgmt.masterresource.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByName(String name);

    boolean existsByCode(String code);
}
