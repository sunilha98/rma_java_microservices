package com.resourcemgmt.usermgmt.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ActivityLogDTOTest {

    private ActivityLogDTO activityLogDTO;

    @BeforeEach
    void setUp() {
        activityLogDTO = new ActivityLogDTO();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instance with no-args constructor")
        void testNoArgsConstructor() {
            // Act
            ActivityLogDTO dto = new ActivityLogDTO();

            // Assert
            assertNotNull(dto);
            assertNull(dto.getAction());
            assertNull(dto.getPerformedBy());
            assertNull(dto.getRole());
            assertNull(dto.getModule());
            assertNull(dto.getDetails());
            assertNull(dto.getTimestamp());
        }

        @Test
        @DisplayName("Should create instance with all-args constructor")
        void testAllArgsConstructor() {
            // Arrange
            LocalDateTime now = LocalDateTime.now();
            
            // Act
            ActivityLogDTO dto = new ActivityLogDTO(
                "CREATE_USER",
                "admin@example.com",
                "ADMIN",
                "User Management",
                "Created user: john.doe@example.com",
                now
            );

            // Assert
            assertNotNull(dto);
            assertEquals("CREATE_USER", dto.getAction());
            assertEquals("admin@example.com", dto.getPerformedBy());
            assertEquals("ADMIN", dto.getRole());
            assertEquals("User Management", dto.getModule());
            assertEquals("Created user: john.doe@example.com", dto.getDetails());
            assertEquals(now, dto.getTimestamp());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get action")
        void testSetAndGetAction() {
            // Arrange
            String action = "UPDATE_USER";

            // Act
            activityLogDTO.setAction(action);
            String retrievedAction = activityLogDTO.getAction();

            // Assert
            assertEquals(action, retrievedAction);
        }

        @Test
        @DisplayName("Should set and get performedBy")
        void testSetAndGetPerformedBy() {
            // Arrange
            String performedBy = "user@example.com";

            // Act
            activityLogDTO.setPerformedBy(performedBy);
            String retrievedPerformedBy = activityLogDTO.getPerformedBy();

            // Assert
            assertEquals(performedBy, retrievedPerformedBy);
        }

        @Test
        @DisplayName("Should set and get role")
        void testSetAndGetRole() {
            // Arrange
            String role = "USER";

            // Act
            activityLogDTO.setRole(role);
            String retrievedRole = activityLogDTO.getRole();

            // Assert
            assertEquals(role, retrievedRole);
        }

        @Test
        @DisplayName("Should set and get module")
        void testSetAndGetModule() {
            // Arrange
            String module = "Authentication";

            // Act
            activityLogDTO.setModule(module);
            String retrievedModule = activityLogDTO.getModule();

            // Assert
            assertEquals(module, retrievedModule);
        }

        @Test
        @DisplayName("Should set and get details")
        void testSetAndGetDetails() {
            // Arrange
            String details = "User logged in successfully";

            // Act
            activityLogDTO.setDetails(details);
            String retrievedDetails = activityLogDTO.getDetails();

            // Assert
            assertEquals(details, retrievedDetails);
        }

        @Test
        @DisplayName("Should set and get timestamp")
        void testSetAndGetTimestamp() {
            // Arrange
            LocalDateTime timestamp = LocalDateTime.now();

            // Act
            activityLogDTO.setTimestamp(timestamp);
            LocalDateTime retrievedTimestamp = activityLogDTO.getTimestamp();

            // Assert
            assertEquals(timestamp, retrievedTimestamp);
        }

        @Test
        @DisplayName("Should handle null values")
        void testHandleNullValues() {
            // Act
            activityLogDTO.setAction(null);
            activityLogDTO.setPerformedBy(null);
            activityLogDTO.setRole(null);
            activityLogDTO.setModule(null);
            activityLogDTO.setDetails(null);
            activityLogDTO.setTimestamp(null);

            // Assert
            assertNull(activityLogDTO.getAction());
            assertNull(activityLogDTO.getPerformedBy());
            assertNull(activityLogDTO.getRole());
            assertNull(activityLogDTO.getModule());
            assertNull(activityLogDTO.getDetails());
            assertNull(activityLogDTO.getTimestamp());
        }

        @Test
        @DisplayName("Should handle empty strings")
        void testHandleEmptyStrings() {
            // Act
            activityLogDTO.setAction("");
            activityLogDTO.setPerformedBy("");
            activityLogDTO.setRole("");
            activityLogDTO.setModule("");
            activityLogDTO.setDetails("");

            // Assert
            assertEquals("", activityLogDTO.getAction());
            assertEquals("", activityLogDTO.getPerformedBy());
            assertEquals("", activityLogDTO.getRole());
            assertEquals("", activityLogDTO.getModule());
            assertEquals("", activityLogDTO.getDetails());
        }

        @Test
        @DisplayName("Should handle special characters in strings")
        void testHandleSpecialCharacters() {
            // Arrange
            String specialString = "Test@#$%^&*()_+-=[]{}|;':\",./<>?";

            // Act
            activityLogDTO.setAction(specialString);
            activityLogDTO.setDetails(specialString);

            // Assert
            assertEquals(specialString, activityLogDTO.getAction());
            assertEquals(specialString, activityLogDTO.getDetails());
        }
    }

    @Nested
    @DisplayName("PrePersist Tests")
    class PrePersistTests {

        @Test
        @DisplayName("Should set timestamp on pre-persist")
        void testOnCreate() {
            // Arrange
            activityLogDTO.setTimestamp(null);

            // Act
            activityLogDTO.onCreate();

            // Assert
            assertNotNull(activityLogDTO.getTimestamp());
            assertTrue(activityLogDTO.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
            assertTrue(activityLogDTO.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(1)));
        }


    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should consider two DTOs equal when all fields are equal")
        void testEqualsWhenAllFieldsEqual() {
            // Arrange
            LocalDateTime timestamp = LocalDateTime.now();
            
            ActivityLogDTO dto1 = new ActivityLogDTO(
                "CREATE_USER",
                "admin@example.com",
                "ADMIN",
                "User Management",
                "Created user",
                timestamp
            );
            
            ActivityLogDTO dto2 = new ActivityLogDTO(
                "CREATE_USER",
                "admin@example.com",
                "ADMIN",
                "User Management",
                "Created user",
                timestamp
            );

            // Assert
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Should consider two DTOs not equal when any field differs")
        void testNotEqualsWhenFieldsDiffer() {
            // Arrange
            LocalDateTime timestamp = LocalDateTime.now();
            
            ActivityLogDTO dto1 = new ActivityLogDTO(
                "CREATE_USER",
                "admin@example.com",
                "ADMIN",
                "User Management",
                "Created user",
                timestamp
            );
            
            ActivityLogDTO dto2 = new ActivityLogDTO(
                "UPDATE_USER",  // Different action
                "admin@example.com",
                "ADMIN",
                "User Management",
                "Created user",
                timestamp
            );

            // Assert
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Should handle null comparison")
        void testEqualsWithNull() {
            // Arrange
            ActivityLogDTO dto = new ActivityLogDTO();

            // Assert
            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("Should handle same object comparison")
        void testEqualsWithSameObject() {
            // Arrange
            ActivityLogDTO dto = new ActivityLogDTO();

            // Assert
            assertEquals(dto, dto);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should include all fields in toString")
        void testToStringContainsAllFields() {
            // Arrange
            LocalDateTime timestamp = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
            ActivityLogDTO dto = new ActivityLogDTO(
                "DELETE_USER",
                "admin@example.com",
                "ADMIN",
                "User Management",
                "Deleted user: john.doe@example.com",
                timestamp
            );

            // Act
            String toString = dto.toString();

            // Assert
            assertNotNull(toString);
            assertTrue(toString.contains("DELETE_USER"));
            assertTrue(toString.contains("admin@example.com"));
            assertTrue(toString.contains("ADMIN"));
            assertTrue(toString.contains("User Management"));
            assertTrue(toString.contains("Deleted user: john.doe@example.com"));
            assertTrue(toString.contains("2024-01-15T10:30"));
        }
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderPatternTests {

        @Test
        @DisplayName("Should work with builder pattern")
        void testBuilderPattern() {
            // Arrange
            LocalDateTime timestamp = LocalDateTime.now();

            // Act
            ActivityLogDTO dto = new ActivityLogDTO();
            dto.setAction("LOGIN");
            dto.setPerformedBy("user@example.com");
            dto.setRole("USER");
            dto.setModule("Authentication");
            dto.setDetails("User logged in successfully");
            dto.setTimestamp(timestamp);

            // Assert
            assertEquals("LOGIN", dto.getAction());
            assertEquals("user@example.com", dto.getPerformedBy());
            assertEquals("USER", dto.getRole());
            assertEquals("Authentication", dto.getModule());
            assertEquals("User logged in successfully", dto.getDetails());
            assertEquals(timestamp, dto.getTimestamp());
        }
    }
}
