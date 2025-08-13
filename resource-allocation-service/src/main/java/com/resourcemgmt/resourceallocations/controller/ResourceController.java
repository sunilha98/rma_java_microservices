package com.resourcemgmt.resourceallocations.controller;

import com.resourcemgmt.resourceallocations.activities.ActivityContextHolder;
import com.resourcemgmt.resourceallocations.activities.ActivityLogService;
import com.resourcemgmt.resourceallocations.activities.LogActivity;
import com.resourcemgmt.resourceallocations.dto.ResourceDTO;
import com.resourcemgmt.resourceallocations.entity.Resource;
import com.resourcemgmt.resourceallocations.entity.Skillset;
import com.resourcemgmt.resourceallocations.entity.Title;
import com.resourcemgmt.resourceallocations.repository.ResourceRepository;
import com.resourcemgmt.resourceallocations.repository.SkillSetRepository;
import com.resourcemgmt.resourceallocations.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private SkillSetRepository skillSetRepository;

    @GetMapping
    public ResponseEntity<List<Resource>> getAllResources() {
        List<Resource> resources = resourceRepository.findByIsActiveTrue();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/bench")
    public ResponseEntity<List<Resource>> getBenchResources() {
        List<Resource> benchResources = resourceRepository
                .findByIsActiveTrueAndBenchStatus(Resource.BenchStatus.AVAILABLE);
        return ResponseEntity.ok(benchResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResourceById(@PathVariable Long id) {
        return resourceRepository.findById(id).map(resource -> ResponseEntity.ok().body(resource))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @LogActivity(action = "Created Resource", module = "Resource Management")
    public ResponseEntity<Resource> createResource(@RequestBody ResourceDTO resourceDto, @RequestHeader("X-Bearer-Token") String token) {
        Resource resource = new Resource();

        Title title = titleRepository.getById(resourceDto.getTitleId());
        List<Skillset> skillsets = skillSetRepository.findAllById(resourceDto.getSkills());

        resource.setFirstName(resourceDto.getFirstName());
        resource.setLastName(resourceDto.getLastName());
        resource.setTitle(title);
        resource.setLocationId(resourceDto.getLocationId());
        resource.setExperience(resourceDto.getExperience());
        resource.setEmail(resourceDto.getEmail());
        resource.setEmployeeId(resourceDto.getEmployeeId());
        resource.setSkillsets(skillsets);

        Resource savedResource = resourceRepository.save(resource);

        ActivityLogService.TOKEN = token;
        ActivityContextHolder.setDetail("Resource", savedResource.getFirstName() + " " + savedResource.getLastName());

        return ResponseEntity.ok(savedResource);
    }

    @PutMapping("/{id}")
    @LogActivity(action = "Updated Resource", module = "Resource Management")
    public ResponseEntity<Resource> updateResource(@PathVariable Long id, @RequestBody Resource resourceDetails, @RequestHeader("X-Bearer-Token") String token) {
        ActivityLogService.TOKEN = token;
        return resourceRepository.findById(id).map(resource -> {
            resource.setFirstName(resourceDetails.getFirstName());
            resource.setLastName(resourceDetails.getLastName());
            resource.setEmail(resourceDetails.getEmail());
            resource.setTitle(resourceDetails.getTitle());
            resource.setLocationId(resourceDetails.getLocationId());
            resource.setPracticeId(resourceDetails.getPracticeId());
            resource.setExperience(resourceDetails.getExperience());
            resource.setSkillsets(resourceDetails.getSkillsets());
            resource.setBenchStatus(resourceDetails.getBenchStatus());
            return ResponseEntity.ok(resourceRepository.save(resource));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/skills")
    public ResponseEntity<List<Skillset>> getSkills() {
        return ResponseEntity.ok(skillSetRepository.findAll());
    }

    @GetMapping("/countActiveResources")
    public Long countActiveResources() {
        return resourceRepository.countActiveResources();
    }

    @GetMapping("/countBenchResources")
    public Long countBenchResources() {
        return resourceRepository.countBenchResources();
    }
}