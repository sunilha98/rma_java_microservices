package com.resourcemgmt.masterresource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.resourcemgmt.masterresource.dto.AuthResponse;
import com.resourcemgmt.masterresource.dto.LoginRequest;
import com.resourcemgmt.masterresource.dto.UserDTO;
import com.resourcemgmt.masterresource.entity.User;
import com.resourcemgmt.masterresource.repository.UserRepository;
import com.resourcemgmt.masterresource.security.JwtUtils;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtils jwtUtils;

	public AuthResponse login(LoginRequest loginRequest) throws AuthenticationException {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		User user = userRepository.findByUsername(loginRequest.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		String token = jwtUtils.generateToken(user.getUsername(), user.getRole().name(), user.getId());

		UserDTO userDTO = new UserDTO(user);

		return new AuthResponse(token, userDTO);
	}

	public User registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
}