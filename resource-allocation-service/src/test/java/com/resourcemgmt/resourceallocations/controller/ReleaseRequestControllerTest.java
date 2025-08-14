package com.resourcemgmt.resourceallocations.controller;

import com.resourcemgmt.resourceallocations.dto.ReleaseRequestDTO;
import com.resourcemgmt.resourceallocations.dto.ReleaseRequestResDTO;
import com.resourcemgmt.resourceallocations.entity.ReleaseRequest;
import com.resourcemgmt.resourceallocations.entity.Resource;
import com.resourcemgmt.resourceallocations.service.ReleaseRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReleaseRequestControllerTest {

    @Mock
    private ReleaseRequestService releaseRequestService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ReleaseRequestController releaseRequestController;

    private ReleaseRequestDTO releaseRequestDTO;
    private ReleaseRequest releaseRequest;
    private Resource resource;
    private Resource replacement;

    @BeforeEach
    void setUp() {
        resource = new Resource();
        resource.setId(1L);
        resource.setFirstName("John");
        resource.setLastName("Doe");

        replacement = new Resource();
        replacement.setId(2L);
        replacement.setFirstName("Jane");
        replacement.setLastName("Smith");

        releaseRequestDTO = new ReleaseRequestDTO();
        releaseRequestDTO.setProjectId(101L);
        releaseRequestDTO.setResourceId(1L);
        releaseRequestDTO.setReplacementId(2L);
        releaseRequestDTO.setReason("Project completed");
        releaseRequestDTO.setEffectiveDate(LocalDateTime.now().plusDays(7));
        releaseRequestDTO.setNotes("Smooth transition needed");

        releaseRequest = new ReleaseRequest();
        releaseRequest.setId(1L);
        releaseRequest.setProjectId(101L);
        releaseRequest.setResource(resource);
        releaseRequest.setReplacement(replacement);
        releaseRequest.setReason("Project completed");
        releaseRequest.setEffectiveDate(LocalDateTime.now().plusDays(7));
        releaseRequest.setNotes("Smooth transition needed");
        releaseRequest.setStatus("PENDING");
    }

    @Test
    void createReleaseRequest_Success() {
        // Mock service response
        when(releaseRequestService.createReleaseRequest(any(ReleaseRequestDTO.class)))
                .thenReturn(releaseRequest);

        // Mock REST template response
        when(restTemplate.exchange(anyString(), any(), any(), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(Collections.singletonMap("name", "Test Project")));

        ResponseEntity<ReleaseRequest> response = releaseRequestController.create(
                releaseRequestDTO, "mock-token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("PENDING", response.getBody().getStatus());

        verify(releaseRequestService, times(1)).createReleaseRequest(any(ReleaseRequestDTO.class));
    }

    @Test
    void getAllReleaseRequests_Success() {
        List<ReleaseRequest> requests = new ArrayList<>();
        requests.add(releaseRequest);

        when(releaseRequestService.getAllRequests()).thenReturn(requests);
        when(restTemplate.exchange(anyString(), any(), any(), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(Collections.singletonMap("name", "Test Project")));

        ResponseEntity<List<ReleaseRequestResDTO>> response =
                releaseRequestController.getAll("mock-token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        ReleaseRequestResDTO dto = response.getBody().get(0);
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("Test Project", dto.getProjectName());
        assertEquals("Jane Smith", dto.getReplacementResource());

        verify(releaseRequestService, times(1)).getAllRequests();
    }

    @Test
    void updateStatus_Success() {
        ResponseEntity<?> response = releaseRequestController.updateStatus(
                1L, Collections.singletonMap("status", "Approved"), "mock-token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(releaseRequestService, times(1)).updateStatus(1L, "Approved");
    }

    @Test
    void updateStatus_Failure() {
        doThrow(new RuntimeException("Error updating status"))
                .when(releaseRequestService).updateStatus(anyLong(), anyString());

        ResponseEntity<?> response = releaseRequestController.updateStatus(
                1L, Collections.singletonMap("status", "Approved"), "mock-token");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to update status", response.getBody());
    }

    @Test
    void getAllReleaseRequests_EmptyList() {
        when(releaseRequestService.getAllRequests()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ReleaseRequestResDTO>> response =
                releaseRequestController.getAll("mock-token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }
}