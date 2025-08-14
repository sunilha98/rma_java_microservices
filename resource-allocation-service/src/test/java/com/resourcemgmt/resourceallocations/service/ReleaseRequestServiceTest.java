package com.resourcemgmt.resourceallocations.service;

import com.resourcemgmt.resourceallocations.dto.ReleaseRequestDTO;
import com.resourcemgmt.resourceallocations.entity.Allocation;
import com.resourcemgmt.resourceallocations.entity.ReleaseRequest;
import com.resourcemgmt.resourceallocations.entity.Resource;
import com.resourcemgmt.resourceallocations.entity.Resource.BenchStatus;
import com.resourcemgmt.resourceallocations.repository.AllocationRepository;
import com.resourcemgmt.resourceallocations.repository.ReleaseRequestRepository;
import com.resourcemgmt.resourceallocations.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReleaseRequestServiceTest {

    @Mock
    private ReleaseRequestRepository releaseRequestRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private AllocationRepository allocationRepository;

    @InjectMocks
    private ReleaseRequestService releaseRequestService;

    private ReleaseRequestDTO requestDTO;
    private Resource resource;
    private Resource replacement;
    private ReleaseRequest releaseRequest;

    @BeforeEach
    void setUp() {
        requestDTO = new ReleaseRequestDTO();
        requestDTO.setProjectId(1L);
        requestDTO.setResourceId(1L);
        requestDTO.setReplacementId(2L);
        requestDTO.setReason("Project ending");
        requestDTO.setEffectiveDate(LocalDateTime.now().plusDays(7));
        requestDTO.setNotes("Sample notes");

        resource = new Resource();
        resource.setId(1L);
        resource.setBenchStatus(BenchStatus.ALLOCATED);
        resource.setFirstName("John");
        resource.setLastName("Doe");

        replacement = new Resource();
        replacement.setId(2L);
        replacement.setBenchStatus(BenchStatus.AVAILABLE);
        replacement.setFirstName("Jane");
        replacement.setLastName("Smith");

        releaseRequest = new ReleaseRequest();
        releaseRequest.setId(1L);
        releaseRequest.setProjectId(1L);
        releaseRequest.setResource(resource);
        releaseRequest.setReplacement(replacement);
        releaseRequest.setStatus("PENDING");
    }

    @Test
    void createReleaseRequest_ShouldCreateAndReturnRequest() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));
        when(resourceRepository.findById(2L)).thenReturn(Optional.of(replacement));
        when(releaseRequestRepository.save(any(ReleaseRequest.class))).thenReturn(releaseRequest);

        ReleaseRequest result = releaseRequestService.createReleaseRequest(requestDTO);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(resource, result.getResource());
        assertEquals(replacement, result.getReplacement());
        verify(releaseRequestRepository, times(1)).save(any(ReleaseRequest.class));
    }

    @Test
    void getAllRequests_ShouldReturnAllRequests() {
        when(releaseRequestRepository.findAll()).thenReturn(Collections.singletonList(releaseRequest));

        List<ReleaseRequest> result = releaseRequestService.getAllRequests();

        assertEquals(1, result.size());
        assertEquals(releaseRequest, result.get(0));
        verify(releaseRequestRepository, times(1)).findAll();
    }

    @Test
    void updateStatus_WhenApproved_ShouldHandleApprovalLogic() {
        // Set a non-null effective date
        releaseRequest.setEffectiveDate(LocalDateTime.now());
        when(releaseRequestRepository.findById(1L)).thenReturn(Optional.of(releaseRequest));

        releaseRequestService.updateStatus(1L, "APPROVED");

        verify(allocationRepository, times(1)).deleteByProjectIdAndResourceId(1L, 1L);
        verify(allocationRepository, times(1)).save(any(Allocation.class));
        verify(resourceRepository, times(1)).save(resource);
        verify(resourceRepository, times(1)).save(replacement);
        assertEquals(BenchStatus.AVAILABLE, resource.getBenchStatus());
        assertEquals(BenchStatus.ALLOCATED, replacement.getBenchStatus());
        assertEquals("APPROVED", releaseRequest.getStatus());
    }



    @Test
    void updateStatus_WhenNotApproved_ShouldOnlyUpdateStatus() {
        when(releaseRequestRepository.findById(1L)).thenReturn(Optional.of(releaseRequest));
        when(releaseRequestRepository.save(any(ReleaseRequest.class))).thenReturn(releaseRequest);

        releaseRequestService.updateStatus(1L, "REJECTED");

        verify(allocationRepository, never()).deleteByProjectIdAndResourceId(anyLong(), anyLong());
        verify(resourceRepository, never()).save(any(Resource.class));
        assertEquals("REJECTED", releaseRequest.getStatus());
    }

    @Test
    void updateStatus_WhenRequestNotFound_ShouldThrowException() {
        when(releaseRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            releaseRequestService.updateStatus(1L, "APPROVED");
        });

        verify(releaseRequestRepository, never()).save(any(ReleaseRequest.class));
    }
}