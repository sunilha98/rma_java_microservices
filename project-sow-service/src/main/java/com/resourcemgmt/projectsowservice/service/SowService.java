package com.resourcemgmt.projectsowservice.service;

import com.resourcemgmt.projectsowservice.dto.SowUploadRequest;
import com.resourcemgmt.projectsowservice.dto.reports.GovernanceDTO;
import com.resourcemgmt.projectsowservice.entity.Project;
import com.resourcemgmt.projectsowservice.entity.Sow;
import com.resourcemgmt.projectsowservice.entity.Sow.Priority;
import com.resourcemgmt.projectsowservice.entity.Sow.SowStatus;
import com.resourcemgmt.projectsowservice.repository.PracticeRepository;
import com.resourcemgmt.projectsowservice.repository.ProjectRepository;
import com.resourcemgmt.projectsowservice.repository.SowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SowService {

    private final PracticeRepository practiceRepository;

    @Autowired
    private SowRepository sowRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RestTemplate restTemplate;

    SowService(PracticeRepository practiceRepository) {
        this.practiceRepository = practiceRepository;
    }

    public void handleSowUpload(MultipartFile file, SowUploadRequest request) throws IOException {

        Sow sow = new Sow();
        sow.setSowNumber(UUID.randomUUID().toString());
        sow.setClientId(request.getClientId());
        sow.setTitle(request.getProjectName());
        sow.setPriority(Priority.valueOf(request.getPriority().toUpperCase()));
        sow.setStatus(SowStatus.DRAFT);
        sow.setDocumentUrl(storeFile(file)); // implement file storage logic
        sow.setCreatedAt(LocalDateTime.now());
        sowRepository.save(sow);

        Project project = new Project();
        project.setProjectCode("PRJ-" + UUID.randomUUID().toString().substring(0, 8));
        project.setName(request.getProjectName());
        project.setClientId(request.getClientId());
        project.setSow(sow);
        project.setStatus("PROPOSED");
        project.setCreatedAt(LocalDateTime.now());
        project.setPractice(practiceRepository.findByName(request.getPositions().get(0).getTitle()));
        projectRepository.save(project);

        // You can also persist positions if needed in a separate table
    }

    private String storeFile(MultipartFile file) throws IOException {
        // Save file to disk or cloud and return URL
        Path path = Paths.get("uploads/" + file.getOriginalFilename());
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        return path.toString();
    }

    public List<GovernanceDTO> getGovernanceReport(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = "http://localhost:8080/api/clients";
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        List<Map<String, Object>> allClients = response.getBody();

        Map<Long, String> clientMap = new HashMap<>();
        allClients.forEach(m -> {
            Long clientId = Long.valueOf(m.get("id").toString());
            String clientName = Objects.toString(m.get("name"), "");
            clientMap.put(clientId, clientName);
        });

        List<GovernanceDTO> governanceDTOS =  sowRepository.findAll().stream().map(s -> new GovernanceDTO(s.getId(), clientMap.get(s.getClientId()),
                String.valueOf(s.getStatus()), s.getUpdatedAt())).collect(Collectors.toList());

        return governanceDTOS;
    }
}
