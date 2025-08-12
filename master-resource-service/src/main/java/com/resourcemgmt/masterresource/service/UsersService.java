package com.resourcemgmt.masterresource.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.resourcemgmt.masterresource.entity.User;
import com.resourcemgmt.masterresource.repository.UserRepository;

@Service
public class UsersService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	public User createUser(User user, String createdBy) {
		user.setCreatedBy(createdBy);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		user.setUpdatedBy(createdBy);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public User updateUser(Long id, User updatedUser, String updatedBy) {
		User user = getUserById(id);
		user.setUsername(updatedUser.getUsername());
		user.setEmail(updatedUser.getEmail());
		user.setRole(updatedUser.getRole());
		user.setIsActive(updatedUser.isActive());
		user.setUpdatedAt(LocalDateTime.now());
		user.setUpdatedBy(updatedBy);
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		return userRepository.save(user);
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}
