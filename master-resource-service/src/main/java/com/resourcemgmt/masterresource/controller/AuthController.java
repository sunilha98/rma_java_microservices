package com.resourcemgmt.masterresource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resourcemgmt.masterresource.dto.AuthResponse;
import com.resourcemgmt.masterresource.dto.LoginRequest;
import com.resourcemgmt.masterresource.dto.UserDTO;
import com.resourcemgmt.masterresource.entity.User;
import com.resourcemgmt.masterresource.repository.UserRepository;
import com.resourcemgmt.masterresource.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			AuthResponse authResponse = authService.login(loginRequest);
			return ResponseEntity.ok(authResponse);
		} catch (Exception e) {
			return ResponseEntity.status(401).body("Invalid credentials");
		}
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

		UserDTO userDTO = new UserDTO(user);
		return ResponseEntity.ok(userDTO);
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			User user = new User();
			user.setUsername(loginRequest.getUsername());
			user.setPassword(loginRequest.getPassword());
			User authResponse = authService.registerUser(user);
			return ResponseEntity.ok(authResponse);
		} catch (Exception e) {
			return ResponseEntity.status(401).body("Invalid credentials");
		}
	}
}