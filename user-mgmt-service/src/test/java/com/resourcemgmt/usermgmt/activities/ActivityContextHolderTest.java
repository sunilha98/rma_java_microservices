package com.resourcemgmt.usermgmt.activities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ActivityContextHolderTest {

    @BeforeEach
    void setUp() {
        // Ensure clean state before each test
        ActivityContextHolder.clear();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        ActivityContextHolder.clear();
    }

    @Test
    @DisplayName("Should set and get detail successfully")
    void testSetAndGetDetail() {
        // Arrange
        String key = "testKey";
        String value = "testValue";

        // Act
        ActivityContextHolder.setDetail(key, value);
        String retrievedValue = ActivityContextHolder.getDetail(key);

        // Assert
        assertEquals(value, retrievedValue);
    }

    @Test
    @DisplayName("Should return null for non-existent key")
    void testGetDetailNonExistentKey() {
        // Act
        String value = ActivityContextHolder.getDetail("nonExistentKey");

        // Assert
        assertNull(value);
    }

    @Test
    @DisplayName("Should return empty map when no details are set")
    void testGetAllDetailsEmpty() {
        // Act
        Map<String, String> details = ActivityContextHolder.getAllDetails();

        // Assert
        assertNotNull(details);
        assertTrue(details.isEmpty());
    }

    @Test
    @DisplayName("Should return all details as map")
    void testGetAllDetails() {
        // Arrange
        ActivityContextHolder.setDetail("key1", "value1");
        ActivityContextHolder.setDetail("key2", "value2");
        ActivityContextHolder.setDetail("key3", "value3");

        // Act
        Map<String, String> details = ActivityContextHolder.getAllDetails();

        // Assert
        assertEquals(3, details.size());
        assertEquals("value1", details.get("key1"));
        assertEquals("value2", details.get("key2"));
        assertEquals("value3", details.get("key3"));
    }

    @Test
    @DisplayName("Should update existing detail")
    void testUpdateExistingDetail() {
        // Arrange
        String key = "testKey";
        String initialValue = "initialValue";
        String updatedValue = "updatedValue";

        // Act
        ActivityContextHolder.setDetail(key, initialValue);
        ActivityContextHolder.setDetail(key, updatedValue);
        String retrievedValue = ActivityContextHolder.getDetail(key);

        // Assert
        assertEquals(updatedValue, retrievedValue);
    }

    @Test
    @DisplayName("Should clear all details")
    void testClear() {
        // Arrange
        ActivityContextHolder.setDetail("key1", "value1");
        ActivityContextHolder.setDetail("key2", "value2");

        // Act
        ActivityContextHolder.clear();
        Map<String, String> details = ActivityContextHolder.getAllDetails();

        // Assert
        assertNotNull(details);
        assertTrue(details.isEmpty());
    }

    @Test
    @DisplayName("Should handle null values")
    void testSetNullValue() {
        // Arrange
        String key = "testKey";

        // Act
        ActivityContextHolder.setDetail(key, null);
        String retrievedValue = ActivityContextHolder.getDetail(key);

        // Assert
        assertNull(retrievedValue);
    }

    @Test
    @DisplayName("Should handle empty string values")
    void testSetEmptyStringValue() {
        // Arrange
        String key = "testKey";
        String emptyValue = "";

        // Act
        ActivityContextHolder.setDetail(key, emptyValue);
        String retrievedValue = ActivityContextHolder.getDetail(key);

        // Assert
        assertEquals(emptyValue, retrievedValue);
    }

    @Test
    @DisplayName("Should handle special characters in keys and values")
    void testSpecialCharacters() {
        // Arrange
        String specialKey = "key@#$%^&*()";
        String specialValue = "value with spaces and symbols!@#$%^&*()";

        // Act
        ActivityContextHolder.setDetail(specialKey, specialValue);
        String retrievedValue = ActivityContextHolder.getDetail(specialKey);

        // Assert
        assertEquals(specialValue, retrievedValue);
    }

    @Test
    @DisplayName("Should maintain thread isolation")
    void testThreadIsolation() throws InterruptedException {
        // Arrange
        ActivityContextHolder.setDetail("thread1", "value1");

        // Create a new thread that sets a different value
        Thread thread = new Thread(() -> {
            ActivityContextHolder.setDetail("thread2", "value2");
            assertEquals("value2", ActivityContextHolder.getDetail("thread2"));
            assertNull(ActivityContextHolder.getDetail("thread1"));
        });

        // Act
        thread.start();
        thread.join();

        // Assert - main thread should still have its original value
        assertEquals("value1", ActivityContextHolder.getDetail("thread1"));
        assertNull(ActivityContextHolder.getDetail("thread2"));
    }

    @Test
    @DisplayName("Should handle multiple operations correctly")
    void testMultipleOperations() {
        // Act & Assert
        ActivityContextHolder.setDetail("key1", "value1");
        assertEquals("value1", ActivityContextHolder.getDetail("key1"));

        ActivityContextHolder.setDetail("key2", "value2");
        assertEquals("value2", ActivityContextHolder.getDetail("key2"));

        ActivityContextHolder.setDetail("key1", "updatedValue1");
        assertEquals("updatedValue1", ActivityContextHolder.getDetail("key1"));

        Map<String, String> allDetails = ActivityContextHolder.getAllDetails();
        assertEquals(2, allDetails.size());
        assertEquals("updatedValue1", allDetails.get("key1"));
        assertEquals("value2", allDetails.get("key2"));

        ActivityContextHolder.clear();
        assertTrue(ActivityContextHolder.getAllDetails().isEmpty());
    }
}
