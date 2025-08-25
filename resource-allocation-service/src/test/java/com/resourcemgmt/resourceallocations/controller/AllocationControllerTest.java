package com.resourcemgmt.resourceallocations.controller;

import com.resourcemgmt.resourceallocations.dto.AllocationDTO;
import com.resourcemgmt.resourceallocations.entity.Allocation;
import com.resourcemgmt.resourceallocations.entity.Resource;
import com.resourcemgmt.resourceallocations.entity.Title;
import com.resourcemgmt.resourceallocations.repository.AllocationRepository;
import com.resourcemgmt.resourceallocations.repository.ResourceRepository;
import com.resourcemgmt.resourceallocations.repository.TitleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AllocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AllocationRepository allocationRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private TitleRepository titleRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AllocationController allocationController;

    private Allocation allocation;
    private AllocationDTO allocationDTO;
    private Resource resource;

    @BeforeEach
    void setUp() {
        // Initialize test data
        resource = new Resource();
        resource.setId(1L);
        resource.setFirstName("John");
        resource.setLastName("Doe");
        resource.setAllocationPercentage(0);
        resource.setBenchStatus(Resource.BenchStatus.AVAILABLE);

        allocation = new Allocation();
        allocation.setId(1L);
        allocation.setProjectId(101L);
        allocation.setResource(resource);
        allocation.setAllocationPercentage(100);
        allocation.setStartDate(LocalDateTime.now());
        allocation.setEndDate(LocalDateTime.now().plusMonths(6));

        allocationDTO = new AllocationDTO();
        allocationDTO.setProjectId(101L);
        allocationDTO.setRole("Developer");
        allocationDTO.setResourceId(1L);
        allocationDTO.setAllocationPercent(100);
        allocationDTO.setStartDate(LocalDateTime.now());
        allocationDTO.setEndDate(LocalDateTime.now().plusMonths(6));
    }


    @Test
    void getAllAllocations_ReturnsAllAllocations() {
        when(allocationRepository.findAll()).thenReturn(List.of(allocation));

        ResponseEntity<List<Allocation>> response = allocationController.getAllAllocations();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(allocationRepository, times(1)).findAll();
    }

    @Test
    void getAllocationsByProject_ReturnsProjectAllocations() {
        when(allocationRepository.findByProjectId(101L)).thenReturn(List.of(allocation));

        ResponseEntity<List<Allocation>> response = allocationController.getAllocationsByProject(101L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(allocationRepository, times(1)).findByProjectId(101L);
    }

    @Test
    void getAllocationsByResource_ReturnsResourceAllocations() {
        when(allocationRepository.findByResourceId(1L)).thenReturn(List.of(allocation));

        ResponseEntity<List<Allocation>> response = allocationController.getAllocationsByResource(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(allocationRepository, times(1)).findByResourceId(1L);
    }
}