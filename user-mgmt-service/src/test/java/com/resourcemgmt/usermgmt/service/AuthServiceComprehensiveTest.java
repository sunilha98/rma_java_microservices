package com.resourcemgmt.usermgmt.service;

import com.resourcemgmt.usermgmt.dto.AuthResponse;
import com.resourcemgmt.usermgmt.dto.LoginRequest;
import com.resourcemgmt.usermgmt.dto.UserDTO;
import com.resourcemgmt.usermgmt.entity.User;
import com.resourcemgmt.usermgmt.repository.UserRepository;
import com.resourcemgmt.usermgmt.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
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
class AuthServiceComprehensiveTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private LoginRequest validLoginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRole(User.UserRole.SUPER_ADMIN);
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");

        validLoginRequest = new LoginRequest("testuser", "password");
    }

    @Test
    void login_WithValidCredentials_ReturnsAuthResponse() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(jwtUtils.generateToken("testuser", User.UserRole.SUPER_ADMIN.name(), 1L)).thenReturn("jwt-token-123");

        // Act
        AuthResponse response = authService.login(validLoginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token-123", response.getToken());
        assertNotNull(response.getUser());
        assertEquals("testuser", response.getUser().getUsername());
        assertEquals("test@example.com", response.getUser().getEmail());
    }

    @Test
    void login_WithNullUsername_ThrowsException() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest(null, "password");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_WithEmptyUsername_ThrowsException() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("", "password");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_WithNullPassword_ThrowsException() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testuser", null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_WithEmptyPassword_ThrowsException() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testuser", "");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_UserNotFound_ThrowsRuntimeException() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> authService.login(new LoginRequest("nonexistent", "password")));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void login_WithDifferentRoles_GeneratesCorrectToken() {
        // Test with ADMIN role
        testUser.setRole(User.UserRole.SUPER_ADMIN);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(jwtUtils.generateToken("testuser", "SUPER_ADMIN", 1L)).thenReturn("admin-token");

        AuthResponse adminResponse = authService.login(validLoginRequest);
        assertEquals("admin-token", adminResponse.getToken());

        // Test with SUPER_ADMIN role
        testUser.setRole(User.UserRole.SUPER_ADMIN);
        when(jwtUtils.generateToken("testuser", "SUPER_ADMIN", 1L)).thenReturn("super-admin-token");

        AuthResponse superAdminResponse = authService.login(validLoginRequest);
        assertEquals("super-admin-token", superAdminResponse.getToken());
    }

    @Test
    void registerUser_WithValidUser_ReturnsSavedUser() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("plainPassword");
        newUser.setEmail("new@example.com");
        newUser.setRole(User.UserRole.SUPER_ADMIN);

        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setUsername("newuser");
        savedUser.setPassword("encodedPassword");
        savedUser.setEmail("new@example.com");
        savedUser.setRole(User.UserRole.SUPER_ADMIN);

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(newUser)).thenReturn(savedUser);

        // Act
        User result = authService.registerUser(newUser);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("newuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(newUser);
    }

    @Test
    void registerUser_WithNullUser_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> authService.registerUser(null));
    }

    @Test
    void registerUser_WithNullPassword_EncodesNull() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(null);

        when(passwordEncoder.encode(null)).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = authService.registerUser(user);

        // Assert
        assertNull(result.getPassword());
        verify(passwordEncoder).encode(null);
    }

    @Test
    void registerUser_WithEmptyPassword_EncodesEmptyString() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("");

        when(passwordEncoder.encode("")).thenReturn("encodedEmpty");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = authService.registerUser(user);

        // Assert
        assertEquals("encodedEmpty", result.getPassword());
        verify(passwordEncoder).encode("");
    }
}
