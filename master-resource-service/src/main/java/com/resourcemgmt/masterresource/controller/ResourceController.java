package com.resourcemgmt.masterresource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resourcemgmt.masterresource.activities.ActivityContextHolder;
import com.resourcemgmt.masterresource.activities.LogActivity;
import com.resourcemgmt.masterresource.dto.ResourceDTO;
import com.resourcemgmt.masterresource.entity.Location;
import com.resourcemgmt.masterresource.entity.Resource;
import com.resourcemgmt.masterresource.entity.Skillset;
import com.resourcemgmt.masterresource.entity.Title;
import com.resourcemgmt.masterresource.repository.LocationRepository;
import com.resourcemgmt.masterresource.repository.ResourceRepository;
import com.resourcemgmt.masterresource.repository.SkillSetRepository;
import com.resourcemgmt.masterresource.repository.TitleRepository;

@RestController
@RequestMapping("/resources")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ResourceController {

	@Autowired
	private ResourceRepository resourceRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private TitleRepository titleRepository;

	@Autowired
	private SkillSetRepository skillSetRepository;

	@GetMapping
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('RMT') or hasRole('PROJECT_MANAGER')")
	public ResponseEntity<List<Resource>> getAllResources() {
		List<Resource> resources = resourceRepository.findByIsActiveTrue();
		return ResponseEntity.ok(resources);
	}

	@GetMapping("/bench")
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('RMT')")
	public ResponseEntity<List<Resource>> getBenchResources() {
		List<Resource> benchResources = resourceRepository
				.findByIsActiveTrueAndBenchStatus(Resource.BenchStatus.AVAILABLE);
		return ResponseEntity.ok(benchResources);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('RMT') or hasRole('PROJECT_MANAGER')")
	public ResponseEntity<Resource> getResourceById(@PathVariable Long id) {
		return resourceRepository.findById(id).map(resource -> ResponseEntity.ok().body(resource))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@LogActivity(action = "Created Resource", module = "Resource Management")
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('RMT')")
	public ResponseEntity<Resource> createResource(@RequestBody ResourceDTO resourceDto) {
		Resource resource = new Resource();

		Title title = titleRepository.getById(resourceDto.getTitleId());
		Location location = locationRepository.getById(resourceDto.getLocationId());
		List<Skillset> skillsets = skillSetRepository.findAllById(resourceDto.getSkills());

		resource.setFirstName(resourceDto.getFirstName());
		resource.setLastName(resourceDto.getLastName());
		resource.setTitle(title);
		resource.setLocation(location);
		resource.setExperience(resourceDto.getExperience());
		resource.setEmail(resourceDto.getEmail());
		resource.setEmployeeId(resourceDto.getEmployeeId());
		resource.setSkillsets(skillsets);

		Resource savedResource = resourceRepository.save(resource);

		ActivityContextHolder.setDetail("Resource", savedResource.getFirstName() + " " + savedResource.getLastName());

		return ResponseEntity.ok(savedResource);
	}

	@PutMapping("/{id}")
	@LogActivity(action = "Updated Resource", module = "Resource Management")
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('RMT')")
	public ResponseEntity<Resource> updateResource(@PathVariable Long id, @RequestBody Resource resourceDetails) {
		return resourceRepository.findById(id).map(resource -> {
			resource.setFirstName(resourceDetails.getFirstName());
			resource.setLastName(resourceDetails.getLastName());
			resource.setEmail(resourceDetails.getEmail());
			resource.setTitle(resourceDetails.getTitle());
			resource.setLocation(resourceDetails.getLocation());
			resource.setPractice(resourceDetails.getPractice());
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
}