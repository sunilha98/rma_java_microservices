package com.resourcemgmt.projectsowservice.dto;

import com.resourcemgmt.projectsowservice.entity.User;

public class UserDTO {

	private Long id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private User.UserRole role;

	// Constructors
	public UserDTO() {
	}

	public UserDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.role = user.getRole();
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public User.UserRole getRole() {
		return role;
	}

	public void setRole(User.UserRole role) {
		this.role = role;
	}
}