package com.resourcemgmt.usermgmt.controller;

import com.resourcemgmt.usermgmt.dto.AuthResponse;
import com.resourcemgmt.usermgmt.dto.LoginRequest;
import com.resourcemgmt.usermgmt.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void authenticateUser_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        AuthResponse authResponse = new AuthResponse("token", null);

        when(authService.login(loginRequest)).thenReturn(authResponse);

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void authenticateUser_InvalidCredentials() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

        when(authService.login(loginRequest)).thenThrow(new RuntimeException("Invalid credentials"));

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }
}