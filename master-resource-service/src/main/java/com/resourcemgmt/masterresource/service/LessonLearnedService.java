package com.resourcemgmt.masterresource.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resourcemgmt.masterresource.dto.LessonLearnedDTO;
import com.resourcemgmt.masterresource.entity.LessonLearned;
import com.resourcemgmt.masterresource.entity.Project;
import com.resourcemgmt.masterresource.repository.LessonLearnedRepository;
import com.resourcemgmt.masterresource.repository.ProjectRepository;

@Service
public class LessonLearnedService {

	@Autowired
	private LessonLearnedRepository repository;

	@Autowired
	private ProjectRepository projectRepository;

	public LessonLearnedDTO createLesson(LessonLearnedDTO dto) {
		Project project = projectRepository.findById(dto.getId()).orElseThrow();

		LessonLearned entity = new LessonLearned();
		entity.setProject(project);
		entity.setTitle(dto.getTitle());
		entity.setDescription(dto.getDescription());
		entity.setCategory(dto.getCategory());
		entity.setCreatedBy(dto.getCreatedBy());
		entity.setUpdatedBy(dto.getCreatedBy());
		entity.setCreatedAt(LocalDateTime.now());
		entity.setUpdatedAt(LocalDateTime.now());

		LessonLearned saved = repository.save(entity);
		return toDTO(saved);
	}

	public LessonLearnedDTO getLessonById(Long id) {
		LessonLearned entity = repository.findById(id).orElseThrow();
		return toDTO(entity);
	}

	public LessonLearnedDTO updateLesson(Long id, LessonLearnedDTO dto) {
		LessonLearned entity = repository.findById(id).orElseThrow();

		entity.setTitle(dto.getTitle());
		entity.setDescription(dto.getDescription());
		entity.setCategory(dto.getCategory());
		entity.setUpdatedBy(dto.getUpdatedBy());
		entity.setUpdatedAt(LocalDateTime.now());

		return toDTO(repository.save(entity));
	}

	public void deleteLesson(Long id) throws Exception {
		if (!repository.existsById(id)) {
			throw new Exception("Lesson not found");
		}
		repository.deleteById(id);
	}

	public List<LessonLearnedDTO> searchLessons(String keyword) {
		return repository.searchByKeyword(keyword).stream().map(this::toDTO).collect(Collectors.toList());
	}

	public List<LessonLearnedDTO> getLessonsByProject(Long projectId) {
		return repository.findByProject_Id(projectId).stream().map(this::toDTO).collect(Collectors.toList());
	}

	private LessonLearnedDTO toDTO(LessonLearned entity) {
		LessonLearnedDTO dto = new LessonLearnedDTO();
		dto.setId(entity.getId());
		dto.setProjectCode(entity.getProject().getProjectCode());
		dto.setTitle(entity.getTitle());
		dto.setDescription(entity.getDescription());
		dto.setCategory(entity.getCategory());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedBy(entity.getUpdatedBy());
		return dto;
	}

	public List<LessonLearnedDTO> getAllLessons() {
		return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}
}
