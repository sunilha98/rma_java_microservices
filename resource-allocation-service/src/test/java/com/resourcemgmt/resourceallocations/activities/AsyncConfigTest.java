package com.resourcemgmt.resourceallocations.activities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

class AsyncConfigTest {

    @Test
    void testActivityExecutorBean() {
        AsyncConfig config = new AsyncConfig();
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) config.activityExecutor();

        assertEquals(5, executor.getCorePoolSize());
        assertEquals(10, executor.getMaxPoolSize());
        assertEquals(100, executor.getQueueCapacity());
        assertTrue(executor.getThreadNamePrefix().startsWith("ActivityLogger-"));
    }
}