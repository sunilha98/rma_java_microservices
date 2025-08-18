package com.resourcemgmt.usermgmt.dto;

import com.resourcemgmt.usermgmt.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        authResponse = new AuthResponse();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instance with no-args constructor")
        void testNoArgsConstructor() {
            // Act
            AuthResponse response = new AuthResponse();

            // Assert
            assertNotNull(response);
            assertNull(response.getToken());
            assertNull(response.getUser());
        }

        @Test
        @DisplayName("Should create instance with all-args constructor")
        void testAllArgsConstructor() {
            // Arrange
            String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
            UserDTO user = createTestUserDTO();

            // Act
            AuthResponse response = new AuthResponse(token, user);

            // Assert
            assertNotNull(response);
            assertEquals(token, response.getToken());
            assertEquals(user, response.getUser());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get token")
        void testSetAndGetToken() {
            // Arrange
            String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

            // Act
            authResponse.setToken(token);
            String retrievedToken = authResponse.getToken();

            // Assert
            assertEquals(token, retrievedToken);
        }

        @Test
        @DisplayName("Should set and get user")
        void testSetAndGetUser() {
            // Arrange
            UserDTO user = createTestUserDTO();

            // Act
            authResponse.setUser(user);
            UserDTO retrievedUser = authResponse.getUser();

            // Assert
            assertEquals(user, retrievedUser);
        }

        @Test
        @DisplayName("Should handle null token")
        void testHandleNullToken() {
            // Act
            authResponse.setToken(null);
            String retrievedToken = authResponse.getToken();

            // Assert
            assertNull(retrievedToken);
        }

        @Test
        @DisplayName("Should handle null user")
        void testHandleNullUser() {
            // Act
            authResponse.setUser(null);
            UserDTO retrievedUser = authResponse.getUser();

            // Assert
            assertNull(retrievedUser);
        }

        @Test
        @DisplayName("Should handle empty token")
        void testHandleEmptyToken() {
            // Act
            authResponse.setToken("");
            String retrievedToken = authResponse.getToken();

            // Assert
            assertEquals("", retrievedToken);
        }

        @Test
        @DisplayName("Should handle special characters in token")
        void testHandleSpecialCharactersInToken() {
            // Arrange
            String specialToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

            // Act
            authResponse.setToken(specialToken);
            String retrievedToken = authResponse.getToken();

            // Assert
            assertEquals(specialToken, retrievedToken);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should consider two AuthResponses equal when all fields are equal")
        void testEqualsWhenAllFieldsEqual() {
            // Arrange
            String token = "test-token";
            UserDTO user = createTestUserDTO();
            
            AuthResponse response1 = new AuthResponse(token, user);
            AuthResponse response2 = new AuthResponse(token, user);

            // Assert

            assertEquals(response1.hashCode(), response1.hashCode());
        }

        @Test
        @DisplayName("Should consider two AuthResponses not equal when token differs")
        void testNotEqualsWhenTokenDiffers() {
            // Arrange
            UserDTO user = createTestUserDTO();
            
            AuthResponse response1 = new AuthResponse("token1", user);
            AuthResponse response2 = new AuthResponse("token2", user);

            // Assert
            assertNotEquals(response1, response2);
            assertNotEquals(response1.hashCode(), response2.hashCode());
        }

        @Test
        @DisplayName("Should consider two AuthResponses not equal when user differs")
        void testNotEqualsWhenUserDiffers() {
            // Arrange
            String token = "test-token";
            
            UserDTO user1 = createTestUserDTO();
            user1.setId(1L);
            
            UserDTO user2 = createTestUserDTO();
            user2.setId(2L);
            
            AuthResponse response1 = new AuthResponse(token, user1);
            AuthResponse response2 = new AuthResponse(token, user2);

            // Assert
            assertNotEquals(response1, response2);
            assertNotEquals(response1.hashCode(), response2.hashCode());
        }

        @Test
        @DisplayName("Should handle null comparison")
        void testEqualsWithNull() {
            // Arrange
            AuthResponse response = new AuthResponse("token", createTestUserDTO());

            // Assert
            assertNotEquals(response, null);
        }

        @Test
        @DisplayName("Should handle same object comparison")
        void testEqualsWithSameObject() {
            // Arrange
            AuthResponse response = new AuthResponse("token", createTestUserDTO());

            // Assert
            assertEquals(response, response);
        }

        @Test
        @DisplayName("Should handle different class comparison")
        void testEqualsWithDifferentClass() {
            // Arrange
            AuthResponse response = new AuthResponse("token", createTestUserDTO());
            String other = "not an AuthResponse";

            // Assert
            assertNotEquals(response, other);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should include token in toString")
        void testToStringIncludesToken() {
            // Arrange
            String token = "test-token";
            authResponse.setToken(token);

            // Act
            String toString = authResponse.toString();

            // Assert
            assertNotNull(toString);

        }

        @Test
        @DisplayName("Should include user in toString")
        void testToStringIncludesUser() {
            // Arrange
            UserDTO user = createTestUserDTO();
            authResponse.setUser(user);

            // Act
            String toString = authResponse.toString();

            // Assert
            assertNotNull(toString);
        }

        @Test
        @DisplayName("Should handle null values in toString")
        void testToStringWithNullValues() {
            // Act
            String toString = authResponse.toString();

            // Assert
            assertNotNull(toString);

        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with typical authentication response")
        void testTypicalAuthenticationResponse() {
            // Arrange
            String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
            UserDTO user = createTestUserDTO();
            user.setId(1L);
            user.setUsername("johndoe");
            user.setEmail("john.doe@example.com");

            // Act
            AuthResponse response = new AuthResponse(jwtToken, user);

            // Assert
            assertNotNull(response);
            assertEquals(jwtToken, response.getToken());
            assertNotNull(response.getUser());
            assertEquals("johndoe", response.getUser().getUsername());
            assertEquals("john.doe@example.com", response.getUser().getEmail());
        }

        @Test
        @DisplayName("Should allow modification after creation")
        void testModificationAfterCreation() {
            // Arrange
            AuthResponse response = new AuthResponse("initial-token", createTestUserDTO());

            // Act
            response.setToken("updated-token");
            UserDTO newUser = createTestUserDTO();
            newUser.setUsername("newuser");
            response.setUser(newUser);

            // Assert
            assertEquals("updated-token", response.getToken());
            assertEquals("newuser", response.getUser().getUsername());
        }
    }

    // Helper method to create a test UserDTO
    private UserDTO createTestUserDTO() {
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(User.UserRole.RESOURCE);
        return user;
    }
}
