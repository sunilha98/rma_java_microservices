package com.resourcemgmt.usermgmt.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instance with no-args constructor")
        void testNoArgsConstructor() {
            // Act
            LoginRequest request = new LoginRequest();

            // Assert
            assertNotNull(request);
            assertNull(request.getUsername());
            assertNull(request.getPassword());
        }

        @Test
        @DisplayName("Should create instance with all-args constructor")
        void testAllArgsConstructor() {
            // Arrange
            String username = "testuser";
            String password = "password123";

            // Act
            LoginRequest request = new LoginRequest(username, password);

            // Assert
            assertNotNull(request);
            assertEquals(username, request.getUsername());
            assertEquals(password, request.getPassword());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get username")
        void testSetAndGetUsername() {
            // Arrange
            String username = "testuser";

            // Act
            loginRequest.setUsername(username);
            String retrievedUsername = loginRequest.getUsername();

            // Assert
            assertEquals(username, retrievedUsername);
        }

        @Test
        @DisplayName("Should set and get password")
        void testSetAndGetPassword() {
            // Arrange
            String password = "password123";

            // Act
            loginRequest.setPassword(password);
            String retrievedPassword = loginRequest.getPassword();

            // Assert
            assertEquals(password, retrievedPassword);
        }

        @Test
        @DisplayName("Should handle null username")
        void testHandleNullUsername() {
            // Act
            loginRequest.setUsername(null);
            String retrievedUsername = loginRequest.getUsername();

            // Assert
            assertNull(retrievedUsername);
        }

        @Test
        @DisplayName("Should handle null password")
        void testHandleNullPassword() {
            // Act
            loginRequest.setPassword(null);
            String retrievedPassword = loginRequest.getPassword();

            // Assert
            assertNull(retrievedPassword);
        }

        @Test
        @DisplayName("Should handle empty username")
        void testHandleEmptyUsername() {
            // Act
            loginRequest.setUsername("");
            String retrievedUsername = loginRequest.getUsername();

            // Assert
            assertEquals("", retrievedUsername);
        }

        @Test
        @DisplayName("Should handle empty password")
        void testHandleEmptyPassword() {
            // Act
            loginRequest.setPassword("");
            String retrievedPassword = loginRequest.getPassword();

            // Assert
            assertEquals("", retrievedPassword);
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {


    }
}
