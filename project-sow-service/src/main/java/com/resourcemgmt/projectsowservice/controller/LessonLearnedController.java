package com.resourcemgmt.projectsowservice.controller;

import java.util.List;

import com.resourcemgmt.projectsowservice.activities.ActivityLogService;
import com.resourcemgmt.projectsowservice.dto.reports.LessonsLearnedReportsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.resourcemgmt.projectsowservice.activities.ActivityContextHolder;
import com.resourcemgmt.projectsowservice.activities.LogActivity;
import com.resourcemgmt.projectsowservice.dto.LessonLearnedDTO;
import com.resourcemgmt.projectsowservice.service.LessonLearnedService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/lessons")
public class LessonLearnedController {

	@Autowired
	private LessonLearnedService lessonService;

	@PostMapping
	@LogActivity(action = "Created Lesson", module = "Lesson Management")
	public ResponseEntity<LessonLearnedDTO> createLesson(@Valid @RequestBody LessonLearnedDTO dto,
														 @RequestHeader("X-Bearer-Token") String token, @RequestHeader("X-Auth-Username") String userName) {
		dto.setCreatedBy(userName);
		LessonLearnedDTO created = lessonService.createLesson(dto);

		ActivityLogService.TOKEN=token;
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
			@Valid @RequestBody LessonLearnedDTO dto, @RequestHeader("X-Bearer-Token") String token, @RequestHeader("X-Auth-Username") String userName) {
		dto.setUpdatedBy(userName);
		ActivityLogService.TOKEN = token;

		LessonLearnedDTO resDto = lessonService.updateLesson(id, dto);

		ActivityContextHolder.setDetail("Lesson", resDto.getTitle());
		return ResponseEntity.ok(resDto);
	}

	@DeleteMapping("/{id}")
	@LogActivity(action = "Deleted Lesson", module = "Lesson Management")
	public ResponseEntity<Void> deleteLesson(@PathVariable Long id, @RequestHeader("X-Bearer-Token") String token, @RequestHeader("X-Auth-Username") String userName) throws Exception {
		lessonService.deleteLesson(id);
		ActivityLogService.TOKEN = token;
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

	@GetMapping("/getLessonsLearnedReports")
	public ResponseEntity<List<LessonsLearnedReportsDTO>> getLessonsLearnedReports() {
		return ResponseEntity.ok(lessonService.getLessonsLearnedReports());
	}
}
