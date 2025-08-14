package com.resourcemgmt.resourceallocations.controller;

import com.resourcemgmt.resourceallocations.dto.ResourceDTO;
import com.resourcemgmt.resourceallocations.entity.Resource;
import com.resourcemgmt.resourceallocations.entity.Skillset;
import com.resourcemgmt.resourceallocations.entity.Title;
import com.resourcemgmt.resourceallocations.repository.ResourceRepository;
import com.resourcemgmt.resourceallocations.repository.SkillSetRepository;
import com.resourcemgmt.resourceallocations.repository.TitleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceControllerTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private TitleRepository titleRepository;

    @Mock
    private SkillSetRepository skillSetRepository;

    @InjectMocks
    private ResourceController resourceController;

    private Resource resource;
    private ResourceDTO resourceDTO;
    private Title title;
    private Skillset skillset;

    @BeforeEach
    void setUp() {
        title = new Title();
        title.setId(1L);
        title.setName("Software Engineer");

        skillset = new Skillset();
        skillset.setId(1L);
        skillset.setName("Java");

        resource = new Resource();
        resource.setId(1L);
        resource.setFirstName("John");
        resource.setLastName("Doe");
        resource.setTitle(title);
        resource.setEmail("john.doe@example.com");
        resource.setEmployeeId("EMP001");
        resource.setSkillsets(List.of(skillset));
        resource.setExperience(BigDecimal.valueOf(5));
        resource.setLocationId(101L);
        resource.setBenchStatus(Resource.BenchStatus.AVAILABLE);
        resource.setIsActive(true);

        resourceDTO = new ResourceDTO();
        resourceDTO.setFirstName("John");
        resourceDTO.setLastName("Doe");
        resourceDTO.setTitleId(1L);
        resourceDTO.setEmail("john.doe@example.com");
        resourceDTO.setEmployeeId("EMP001");
        resourceDTO.setSkills(List.of(1L));
        resourceDTO.setExperience(BigDecimal.valueOf(5));
        resourceDTO.setLocationId(101L);
    }

    @Test
    void getAllResources_ReturnsActiveResources() {
        when(resourceRepository.findByIsActiveTrue()).thenReturn(List.of(resource));

        ResponseEntity<List<Resource>> response = resourceController.getAllResources();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("John", response.getBody().get(0).getFirstName());
        verify(resourceRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    void getBenchResources_ReturnsAvailableResources() {
        when(resourceRepository.findByIsActiveTrueAndBenchStatus(Resource.BenchStatus.AVAILABLE))
                .thenReturn(List.of(resource));

        ResponseEntity<List<Resource>> response = resourceController.getBenchResources();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(Resource.BenchStatus.AVAILABLE, response.getBody().get(0).getBenchStatus());
        verify(resourceRepository, times(1))
                .findByIsActiveTrueAndBenchStatus(Resource.BenchStatus.AVAILABLE);
    }

    @Test
    void getResourceById_ExistingId_ReturnsResource() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));

        ResponseEntity<Resource> response = resourceController.getResourceById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        verify(resourceRepository, times(1)).findById(1L);
    }

    @Test
    void getResourceById_NonExistingId_ReturnsNotFound() {
        when(resourceRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Resource> response = resourceController.getResourceById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(resourceRepository, times(1)).findById(99L);
    }

    @Test
    void createResource_Success() {
        when(titleRepository.getById(1L)).thenReturn(title);
        when(skillSetRepository.findAllById(any())).thenReturn(List.of(skillset));
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        ResponseEntity<Resource> response = resourceController.createResource(resourceDTO, "mock-token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        verify(resourceRepository, times(1)).save(any(Resource.class));
    }

    @Test
    void updateResource_ExistingId_Success() {
        Resource updatedResource = new Resource();
        updatedResource.setFirstName("John Updated");
        updatedResource.setLastName("Doe Updated");
        updatedResource.setEmail("john.updated@example.com");

        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));
        when(resourceRepository.save(any(Resource.class))).thenReturn(updatedResource);

        ResponseEntity<Resource> response = resourceController.updateResource(1L, updatedResource, "mock-token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Updated", response.getBody().getFirstName());
        verify(resourceRepository, times(1)).findById(1L);
        verify(resourceRepository, times(1)).save(any(Resource.class));
    }

    @Test
    void updateResource_NonExistingId_ReturnsNotFound() {
        when(resourceRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Resource> response = resourceController.updateResource(99L, new Resource(), "mock-token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(resourceRepository, times(1)).findById(99L);
        verify(resourceRepository, never()).save(any());
    }

    @Test
    void getSkills_ReturnsAllSkills() {
        when(skillSetRepository.findAll()).thenReturn(List.of(skillset));

        ResponseEntity<List<Skillset>> response = resourceController.getSkills();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Java", response.getBody().get(0).getName());
        verify(skillSetRepository, times(1)).findAll();
    }

    @Test
    void countActiveResources_ReturnsCount() {
        when(resourceRepository.countActiveResources()).thenReturn(10L);

        Long count = resourceController.countActiveResources();

        assertEquals(10L, count);
        verify(resourceRepository, times(1)).countActiveResources();
    }

    @Test
    void countBenchResources_ReturnsCount() {
        when(resourceRepository.countBenchResources()).thenReturn(5L);

        Long count = resourceController.countBenchResources();

        assertEquals(5L, count);
        verify(resourceRepository, times(1)).countBenchResources();
    }
}