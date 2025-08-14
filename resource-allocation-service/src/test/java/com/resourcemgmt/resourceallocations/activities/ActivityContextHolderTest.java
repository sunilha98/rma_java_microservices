package com.resourcemgmt.resourceallocations.activities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class ActivityContextHolderTest {

    @BeforeEach
    @AfterEach
    void clearContext() {
        ActivityContextHolder.clear();
    }

    @Test
    void testSetAndGetDetail() {
        ActivityContextHolder.setDetail("key1", "value1");
        assertEquals("value1", ActivityContextHolder.getDetail("key1"));
    }

    @Test
    void testGetDetail_WhenKeyNotExists() {
        assertNull(ActivityContextHolder.getDetail("nonexistent"));
    }

    @Test
    void testGetAllDetails() {
        ActivityContextHolder.setDetail("key1", "value1");
        ActivityContextHolder.setDetail("key2", "value2");

        Map<String, String> details = ActivityContextHolder.getAllDetails();
        assertEquals(2, details.size());
        assertEquals("value1", details.get("key1"));
        assertEquals("value2", details.get("key2"));
    }

    @Test
    void testGetAllDetails_WhenEmpty() {
        Map<String, String> details = ActivityContextHolder.getAllDetails();
        assertTrue(details.isEmpty());
    }

    @Test
    void testClear() {
        ActivityContextHolder.setDetail("key1", "value1");
        ActivityContextHolder.clear();
        assertNull(ActivityContextHolder.getDetail("key1"));
    }

    @Test
    void testThreadIsolation() throws InterruptedException {
        ActivityContextHolder.setDetail("mainThread", "mainValue");

        Thread otherThread = new Thread(() -> {
            ActivityContextHolder.setDetail("otherThread", "otherValue");
            assertEquals("otherValue", ActivityContextHolder.getDetail("otherThread"));
            assertNull(ActivityContextHolder.getDetail("mainThread"));
        });

        otherThread.start();
        otherThread.join();

        assertEquals("mainValue", ActivityContextHolder.getDetail("mainThread"));
        assertNull(ActivityContextHolder.getDetail("otherThread"));
    }
}