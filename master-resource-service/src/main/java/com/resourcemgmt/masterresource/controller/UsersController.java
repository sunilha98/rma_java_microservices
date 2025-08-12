package com.resourcemgmt.masterresource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resourcemgmt.masterresource.activities.ActivityContextHolder;
import com.resourcemgmt.masterresource.activities.LogActivity;
import com.resourcemgmt.masterresource.entity.User;
import com.resourcemgmt.masterresource.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UsersService userService;

	@GetMapping
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PROJECT_MANAGER')")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PROJECT_MANAGER')")
	public User getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}

	@PostMapping
	@LogActivity(action = "Created user", module = "User Management")
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PROJECT_MANAGER')")
	public User createUser(@RequestBody User user) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String createdBy = authentication.getName();

		User resUser = userService.createUser(user, createdBy);

		ActivityContextHolder.setDetail("User", resUser.getFirstName() + " " + resUser.getLastName());

		return resUser;
	}

	@PutMapping("/{id}")
	@LogActivity(action = "Updated user", module = "User Management")
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PROJECT_MANAGER')")
	public User updateUser(@PathVariable Long id, @RequestBody User user) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String updatedBy = authentication.getName();
		User resUser = userService.updateUser(id, user, updatedBy);

		ActivityContextHolder.setDetail("User", resUser.getFirstName() + " " + resUser.getLastName());

		return resUser;
	}

	@DeleteMapping("/{id}")
	@LogActivity(action = "Deleted user", module = "User Management")
	@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PROJECT_MANAGER')")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);

		ActivityContextHolder.setDetail("User Id", id.toString());
		return ResponseEntity.ok().build();
	}
}
