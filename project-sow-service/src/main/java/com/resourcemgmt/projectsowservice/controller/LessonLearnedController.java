package com.resourcemgmt.projectsowservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resourcemgmt.projectsowservice.activities.ActivityContextHolder;
import com.resourcemgmt.projectsowservice.activities.LogActivity;
import com.resourcemgmt.projectsowservice.dto.LessonLearnedDTO;
import com.resourcemgmt.projectsowservice.service.LessonLearnedService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/lessons")
@Validated
public class LessonLearnedController {

	@Autowired
	private LessonLearnedService lessonService;

	@PostMapping
	@LogActivity(action = "Created Lesson", module = "Lesson Management")
	public ResponseEntity<LessonLearnedDTO> createLesson(@Valid @RequestBody LessonLearnedDTO dto,
			@AuthenticationPrincipal UserDetails user) {
		dto.setCreatedBy(user.getUsername());
		LessonLearnedDTO created = lessonService.createLesson(dto);

		ActivityContextHolder.setDetail("Lesson", dto.getTitle());

		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	public ResponseEntity<List<LessonLearnedDTO>> getAllLessons() {
		return ResponseEntity.ok(lessonService.getAllLessons());
	}

	@GetMapping("/{id}")
	public ResponseEntity<LessonLearnedDTO> getLesson(@PathVariable Long id) {
		return ResponseEntity.ok(lessonService.getLessonById(id));
	}

	@PutMapping("/{id}")
	@LogActivity(action = "Updated Lesson", module = "Lesson Management")
	public ResponseEntity<LessonLearnedDTO> updateLesson(@PathVariable Long id,
			@Valid @RequestBody LessonLearnedDTO dto, @AuthenticationPrincipal UserDetails user) {
		dto.setUpdatedBy(user.getUsername());

		LessonLearnedDTO resDto = lessonService.updateLesson(id, dto);

		ActivityContextHolder.setDetail("Lesson", resDto.getTitle());
		return ResponseEntity.ok(resDto);
	}

	@DeleteMapping("/{id}")
	@LogActivity(action = "Deleted Lesson", module = "Lesson Management")
	public ResponseEntity<Void> deleteLesson(@PathVariable Long id) throws Exception {
		lessonService.deleteLesson(id);
		ActivityContextHolder.setDetail("Lesson id: ", id.toString());
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	public ResponseEntity<List<LessonLearnedDTO>> searchLessons(@RequestParam String keyword) {
		return ResponseEntity.ok(lessonService.searchLessons(keyword));
	}

	@GetMapping("/project/{projectId}")
	public ResponseEntity<List<LessonLearnedDTO>> getLessonsByProject(@PathVariable Long projectId) {
		return ResponseEntity.ok(lessonService.getLessonsByProject(projectId));
	}
}
