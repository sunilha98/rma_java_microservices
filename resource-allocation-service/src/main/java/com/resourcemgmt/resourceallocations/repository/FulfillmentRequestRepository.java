package com.resourcemgmt.resourceallocations.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.resourceallocations.entity.FulfillmentRequest;

public interface FulfillmentRequestRepository extends JpaRepository<FulfillmentRequest, UUID> {
}
