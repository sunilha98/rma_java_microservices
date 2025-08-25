package com.resourcemgmt.resourceallocations.controller;

import com.resourcemgmt.resourceallocations.activities.ActivityContextHolder;
import com.resourcemgmt.resourceallocations.activities.ActivityLogService;
import com.resourcemgmt.resourceallocations.activities.LogActivity;
import com.resourcemgmt.resourceallocations.dto.FulfillmentCreationRequestDTO;
import com.resourcemgmt.resourceallocations.dto.FulfillmentRequestDTO;
import com.resourcemgmt.resourceallocations.entity.FulfillmentRequest;
import com.resourcemgmt.resourceallocations.entity.Skillset;
import com.resourcemgmt.resourceallocations.repository.FulfillmentRequestRepository;
import com.resourcemgmt.resourceallocations.repository.SkillSetRepository;
import com.resourcemgmt.resourceallocations.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/fulfillment-requests")
public class FulfillmentRequestController {

    @Autowired
    private FulfillmentRequestRepository repository;

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private SkillSetRepository skillsetRepository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<List<FulfillmentRequestDTO>> getAllRequests(@RequestHeader("X-Bearer-Token") String token) {
        List<FulfillmentRequest> requests = repository.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);


        List<FulfillmentRequestDTO> dtos = requests.stream().map(req -> {

            String url = "http://api-gateway:8080/api/locations/" + req.getLocationId();
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            String location = response.getBody().get("name").toString();

            String url2 = "http://api-gateway:8080/api/shifts/" + req.getShiftId();
            ResponseEntity<Map> response2 = restTemplate.exchange(url2, HttpMethod.GET, entity, Map.class);
            String shift = response2.getBody().get("name").toString();

            String url3 = "http://api-gateway:8080/api/projects/" + req.getProjectId();
            ResponseEntity<Map> response3 = restTemplate.exchange(url3, HttpMethod.GET, entity, Map.class);
            String projectName = response3.getBody().get("name").toString();

            FulfillmentRequestDTO dto = new FulfillmentRequestDTO();
            dto.setId(req.getId());
            dto.setProjectName(projectName);
            dto.setTitle(req.getTitle().getName());
            dto.setSkills(req.getSkillsets().stream().map(Skillset::getName).toList());
            dto.setLocation(location);
            dto.setStatus(req.getStatus().name());
            dto.setExpectedClosure(req.getExpectedClosure());
            dto.setNotes(req.getNotes());
            dto.setExperience(req.getExperience());
            dto.setPositions(req.getPositions());
            dto.setShift(shift);
            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @LogActivity(action = "Updated FullFillMent", module = "FullFillMent Management")
    public ResponseEntity<?> updateStatusAndNotes(@PathVariable UUID id, @RequestBody Map<String, Object> updates, @RequestHeader("X-Bearer-Token") String token) {
        Optional<FulfillmentRequest> optional = repository.findById(id);
        if (optional.isEmpty())
            return ResponseEntity.notFound().build();

        FulfillmentRequest request = optional.get();
        if (updates.containsKey("status")) {
            request.setStatus(FulfillmentRequest.Status.valueOf((String) updates.get("status")));
        }
        if (updates.containsKey("notes")) {
            request.setNotes((String) updates.get("notes"));
        }

        repository.save(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = "http://api-gateway:8080/api/projects/" + request.getProjectId();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        String projectName = response.getBody().get("name").toString();

        ActivityLogService.TOKEN = token;
        ActivityContextHolder.setDetail("Project", projectName);
        ActivityContextHolder.setDetail("Title", request.getTitle().getName());

        return ResponseEntity.ok().build();
    }

    @PostMapping
    @LogActivity(action = "Created FullFillMent", module = "FullFillMent Management")
    public ResponseEntity<?> createFulfillmentRequest(@RequestBody FulfillmentCreationRequestDTO dto,@RequestHeader("X-Bearer-Token") String token) {
        FulfillmentRequest request = new FulfillmentRequest();

        request.setProjectId(dto.getProjectId());
        request.setTitle(titleRepository.findById(dto.getTitleId()).orElse(null));
        request.setLocationId(dto.getLocationId());
        request.setShiftId(dto.getShiftId());
        request.setExperience(dto.getExperience());
        request.setPositions(dto.getPositions());
        request.setExpectedClosure(dto.getExpectedClosure());
        request.setNotes(dto.getNotes());
        request.setStatus(FulfillmentRequest.Status.OPEN);

        // Set skillsets
        List<Skillset> skills = skillsetRepository.findAllById(dto.getSkillsetIds());
        request.setSkillsets(skills);

        FulfillmentRequest saved = repository.save(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = "http://api-gateway:8080/api/projects/" + request.getProjectId();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        String projectName = response.getBody().get("name").toString();

        ActivityLogService.TOKEN = token;
        ActivityContextHolder.setDetail("Project", projectName);
        ActivityContextHolder.setDetail("Title", saved.getTitle().getName());

        return ResponseEntity.ok(saved);
    }

}
