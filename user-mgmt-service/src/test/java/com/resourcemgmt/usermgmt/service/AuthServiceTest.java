package com.resourcemgmt.usermgmt.service;

import com.resourcemgmt.usermgmt.dto.AuthResponse;
import com.resourcemgmt.usermgmt.dto.LoginRequest;
import com.resourcemgmt.usermgmt.dto.UserDTO;
import com.resourcemgmt.usermgmt.entity.User;
import com.resourcemgmt.usermgmt.repository.UserRepository;
import com.resourcemgmt.usermgmt.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setRole(User.UserRole.SUPER_ADMIN);
        user.setId(1L);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken("testuser", "SUPER_ADMIN", 1L)).thenReturn("token");

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("token", response.getToken());
        assertNotNull(response.getUser());
    }

    @Test
    void login_UserNotFound() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void registerUser() {
        // Arrange
        User user = new User();
        user.setPassword("password");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = authService.registerUser(user);

        // Assert
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(user);
    }
}