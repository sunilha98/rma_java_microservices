package com.resourcemgmt.resourceallocations.controller;

import com.resourcemgmt.resourceallocations.activities.ActivityContextHolder;
import com.resourcemgmt.resourceallocations.activities.ActivityLogService;
import com.resourcemgmt.resourceallocations.activities.LogActivity;
import com.resourcemgmt.resourceallocations.dto.AllocationDTO;
import com.resourcemgmt.resourceallocations.dto.reports.BenchResourceDTO;
import com.resourcemgmt.resourceallocations.dto.reports.ResourceAllocationDTO;
import com.resourcemgmt.resourceallocations.entity.Allocation;
import com.resourcemgmt.resourceallocations.entity.Resource;
import com.resourcemgmt.resourceallocations.entity.Resource.BenchStatus;
import com.resourcemgmt.resourceallocations.entity.Skillset;
import com.resourcemgmt.resourceallocations.repository.AllocationRepository;
import com.resourcemgmt.resourceallocations.repository.ResourceRepository;
import com.resourcemgmt.resourceallocations.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/allocations")
public class AllocationController {

    private final ResourceRepository resourceRepository;

    private final TitleRepository titleRepository;

    private final RestTemplate restTemplate;

    @Autowired
    private AllocationRepository allocationRepository;

    AllocationController(TitleRepository titleRepository,
                         ResourceRepository resourceRepository, RestTemplate restTemplate) {
        this.titleRepository = titleRepository;
        this.resourceRepository = resourceRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Allocation>> getAllAllocations() {
        List<Allocation> allocations = allocationRepository.findAll();
        return ResponseEntity.ok(allocations);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Allocation>> getAllocationsByProject(@PathVariable Long projectId) {
        List<Allocation> allocations = allocationRepository.findByProjectId(projectId);
        return ResponseEntity.ok(allocations);
    }

    @GetMapping("/resource/{resourceId}")
    public ResponseEntity<List<Allocation>> getAllocationsByResource(@PathVariable Long resourceId) {
        List<Allocation> allocations = allocationRepository.findByResourceId(resourceId);
        return ResponseEntity.ok(allocations);
    }

    @PostMapping
    @LogActivity(action = "Created Allocation", module = "Allocation Management")
    public ResponseEntity<Allocation> createAllocation(@RequestBody AllocationDTO allocationDTO, @RequestHeader("X-Bearer-Token") String token) {

        Allocation allocation = new Allocation();
        Optional<Resource> resourceOptional = resourceRepository.findById(allocationDTO.getResourceId());
        Resource resource = resourceOptional.get();

        allocation.setProjectId(allocationDTO.getProjectId());
        allocation.setTitle(titleRepository.findByName(allocationDTO.getRole()));
        allocation.setResource(resource);

        allocation.setAllocationPercentage(allocationDTO.getAllocationPercent());
        allocation.setStartDate(allocationDTO.getStartDate());
        allocation.setEndDate(allocationDTO.getEndDate());

        Allocation savedAllocation = allocationRepository.save(allocation);

        int prevPercent = Optional.ofNullable(resource.getAllocationPercentage())
                .orElse(0);
        int currentPercent = Optional.ofNullable(allocationDTO.getAllocationPercent()).orElse(0);
        int totalPercent = prevPercent + currentPercent;

        if (totalPercent < 100) {
            resource.setBenchStatus(BenchStatus.AVAILABLE);
        } else {
            resource.setBenchStatus(BenchStatus.ALLOCATED);
        }
        resource.setAllocationPercentage(totalPercent);
        resourceRepository.saveAndFlush(resource);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = "http://localhost:8080/api/projects/" + allocationDTO.getProjectId();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        String projectName = response.getBody().get("name").toString();

        ActivityLogService.TOKEN = token;
        ActivityContextHolder.setDetail("Project", projectName);
        ActivityContextHolder.setDetail("Resource",
                savedAllocation.getResource().getFirstName() + " " + savedAllocation.getResource().getLastName());

        return ResponseEntity.ok(savedAllocation);
    }

    @PutMapping("/{id}")
    @LogActivity(action = "Updated Allocation", module = "Allocation Management")
    public ResponseEntity<Allocation> updateAllocation(@PathVariable Long id,
                                                       @RequestBody Allocation allocationDetails, @RequestHeader("X-Bearer-Token") String token) {
        ActivityLogService.TOKEN = token;
        return allocationRepository.findById(id).map(allocation -> {
            allocation.setAllocationPercentage(allocationDetails.getAllocationPercentage());
            allocation.setStartDate(allocationDetails.getStartDate());
            allocation.setEndDate(allocationDetails.getEndDate());
            allocation.setStatus(allocationDetails.getStatus());
            return ResponseEntity.ok(allocationRepository.save(allocation));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getResourceAllocationReport")
    public ResponseEntity<List<ResourceAllocationDTO>> getResourceAllocationReport(@RequestHeader("X-Bearer-Token") String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = "http://localhost:8080/api/projects";
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        List<Map<String, Object>> resList = response.getBody();
        Map<Long, String> projectMap = resList.stream()
                .collect(Collectors.toMap(
                        m -> Long.valueOf(m.get("id").toString()),
                        m -> Objects.toString(m.get("name"), "")
                ));

        List<ResourceAllocationDTO> report = allocationRepository.findAll().stream()
                .map(a -> {
                    Resource resource = a.getResource();
                    String skillNames = resource.getSkillsets().stream()
                            .map(Skillset::getName)
                            .collect(Collectors.joining(", "));

                    return new ResourceAllocationDTO(
                            resource.getId(),
                            resource.getFirstName(),
                            resource.getLastName(),
                            resource.getTitle() != null ? resource.getTitle().getName() : null,
                            skillNames,
                            a.getProjectId() != null ? projectMap.get(a.getProjectId()) : null,
                            a.getAllocationPercentage()
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(report);
    }

    @GetMapping("/getBenchTrackingReport")
    public ResponseEntity<List<BenchResourceDTO>> getBenchTrackingReport() {
        List<Long> allocatedIds = allocationRepository.findAllAllocatedResourceIds();

        List<BenchResourceDTO> benchResources = resourceRepository.findAll().stream()
                .filter(r -> !allocatedIds.contains(r.getId()))
                .map(r -> {
                    String skillNames = r.getSkillsets().stream()
                            .map(Skillset::getName)
                            .collect(Collectors.joining(", "));

                    return new BenchResourceDTO(
                            r.getId(),
                            r.getFirstName(),
                            r.getLastName(),
                            r.getTitle() != null ? r.getTitle().getName() : null,
                            skillNames,
                            true
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(benchResources);
    }
}