package com.resourcemgmt.masterresource.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resourcemgmt.masterresource.entity.FulfillmentRequest;

public interface FulfillmentRequestRepository extends JpaRepository<FulfillmentRequest, UUID> {
}
