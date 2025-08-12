package com.resourcemgmt.masterresource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resourcemgmt.masterresource.activities.ActivityContextHolder;
import com.resourcemgmt.masterresource.activities.LogActivity;
import com.resourcemgmt.masterresource.dto.ShiftTimingDTO;
import com.resourcemgmt.masterresource.entity.ShiftTiming;
import com.resourcemgmt.masterresource.repository.ShiftTimingRepository;

@RestController
@RequestMapping("/shifts")
public class ShiftTimingController {

	@Autowired
	private ShiftTimingRepository shiftRepo;

	@PostMapping
	@LogActivity(action = "Created Shift", module = "Shift Management")
	public ResponseEntity<?> createShift(@RequestBody ShiftTimingDTO dto) {
		if (shiftRepo.existsByName(dto.getName())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Shift already exists");
		}

		ShiftTiming shift = new ShiftTiming();
		shift.setName(dto.getName());
		shift.setStartTime(dto.getStartTime());
		shift.setEndTime(dto.getEndTime());

		shiftRepo.save(shift);

		ActivityContextHolder.setDetail("Shift", shift.getName());

		return ResponseEntity.ok("Shift created successfully");
	}

	@GetMapping
	public ResponseEntity<List<ShiftTiming>> getAllShifts() {
		return ResponseEntity.ok(shiftRepo.findAll());
	}
}
