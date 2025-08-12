package com.resourcemgmt.projectsowservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resourcemgmt.projectsowservice.dto.SowUploadRequest;
import com.resourcemgmt.projectsowservice.entity.Client;
import com.resourcemgmt.projectsowservice.entity.Project;
import com.resourcemgmt.projectsowservice.entity.Sow;
import com.resourcemgmt.projectsowservice.entity.Sow.Priority;
import com.resourcemgmt.projectsowservice.entity.Sow.SowStatus;
import com.resourcemgmt.projectsowservice.repository.ClientRepository;
import com.resourcemgmt.projectsowservice.repository.PracticeRepository;
import com.resourcemgmt.projectsowservice.repository.ProjectRepository;
import com.resourcemgmt.projectsowservice.repository.SowRepository;

@Service
public class SowService {

	private final PracticeRepository practiceRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private SowRepository sowRepository;

	@Autowired
	private ProjectRepository projectRepository;

	SowService(PracticeRepository practiceRepository) {
		this.practiceRepository = practiceRepository;
	}

	public void handleSowUpload(MultipartFile file, SowUploadRequest request) throws IOException {
		Client client = clientRepository.findByName(request.getClientName())
				.orElseThrow(() -> new RuntimeException("Client not found"));

		Sow sow = new Sow();
		sow.setSowNumber(UUID.randomUUID().toString());
		sow.setClient(client);
		sow.setTitle(request.getProjectName());
		sow.setPriority(Priority.valueOf(request.getPriority().toUpperCase()));
		sow.setStatus(SowStatus.DRAFT);
		sow.setDocumentUrl(storeFile(file)); // implement file storage logic
		sow.setCreatedAt(LocalDateTime.now());
		sowRepository.save(sow);

		Project project = new Project();
		project.setProjectCode("PRJ-" + UUID.randomUUID().toString().substring(0, 8));
		project.setName(request.getProjectName());
		project.setClient(client);
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
}
