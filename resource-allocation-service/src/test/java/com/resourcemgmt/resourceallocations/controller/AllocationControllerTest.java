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

    /*@Test
    void createAllocation_Success() {
        // Arrange
        AllocationDTO allocationDTO = new AllocationDTO();
        allocationDTO.setProjectId(1L);
        allocationDTO.setRole("Developer");
        allocationDTO.setResourceId(1L);
        allocationDTO.setAllocationPercent(50);
        allocationDTO.setStartDate(LocalDateTime.now());
        allocationDTO.setEndDate(LocalDateTime.now().plusMonths(6));

        Resource resource = new Resource();
        resource.setId(1L);
        resource.setFirstName("John");
        resource.setLastName("Doe");
        resource.setAllocationPercentage(0);
        resource.setBenchStatus(Resource.BenchStatus.AVAILABLE);

        Title title = new Title();
        title.setName("Developer");

        Allocation expectedAllocation = new Allocation();
        expectedAllocation.setProjectId(1L);
        expectedAllocation.setResource(resource);
        expectedAllocation.setTitle(title);
        expectedAllocation.setAllocationPercentage(50);

        // Mock repository responses
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));
        when(titleRepository.findByName("Developer")).thenReturn(title);
        when(allocationRepository.save(any(Allocation.class))).thenReturn(expectedAllocation);
        when(resourceRepository.saveAndFlush(any(Resource.class))).thenReturn(resource);

        // Mock REST template response
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("mock-token");
        when(restTemplate.exchange(
                        eq("http://localhost:8080/api/projects/1"),
                        eq(HttpMethod.GET),
                        any(HttpEntity.class),
                        eq(Map.class))
                .thenReturn(ResponseEntity.ok(Collections.singletonMap("name", "Test Project")));

        // Act
        ResponseEntity<Allocation> response = allocationController.createAllocation(allocationDTO, "mock-token");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getProjectId());
        assertEquals(50, response.getBody().getAllocationPercentage());

        // Verify interactions
        verify(resourceRepository).findById(1L);
        verify(titleRepository).findByName("Developer");
        verify(allocationRepository).save(any(Allocation.class));
        verify(resourceRepository).saveAndFlush(any(Resource.class));
    }*/

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