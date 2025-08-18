package com.resourcemgmt.usermgmt.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
    }

    @Test
    @DisplayName("Should create RestTemplate bean successfully")
    void testRestTemplateBeanCreation() {
        // Act
        RestTemplate restTemplate = appConfig.restTemplate();

        // Assert
        assertNotNull(restTemplate, "RestTemplate should not be null");
        assertTrue(restTemplate instanceof RestTemplate, "Should return an instance of RestTemplate");
    }

    @Test
    @DisplayName("Should return new RestTemplate instance on each call")
    void testRestTemplateNewInstance() {
        // Act
        RestTemplate restTemplate1 = appConfig.restTemplate();
        RestTemplate restTemplate2 = appConfig.restTemplate();

        // Assert
        assertNotNull(restTemplate1);
        assertNotNull(restTemplate2);
        assertNotSame(restTemplate1, restTemplate2, "Each call should return a new RestTemplate instance");
    }

    @Test
    @DisplayName("Should have default RestTemplate configuration")
    void testRestTemplateDefaultConfiguration() {
        // Act
        RestTemplate restTemplate = appConfig.restTemplate();

        // Assert
        assertNotNull(restTemplate);
        assertNotNull(restTemplate.getMessageConverters());
        assertFalse(restTemplate.getMessageConverters().isEmpty(), "Should have default message converters");
    }

    @Test
    @DisplayName("Should be annotated with Configuration")
    void testConfigurationAnnotation() {
        // Assert
        assertTrue(AppConfig.class.isAnnotationPresent(org.springframework.context.annotation.Configuration.class),
                "AppConfig should be annotated with @Configuration");
    }

    @Test
    @DisplayName("Should have RestTemplate method annotated with Bean")
    void testRestTemplateBeanAnnotation() throws NoSuchMethodException {
        // Assert
        assertTrue(AppConfig.class.getDeclaredMethod("restTemplate")
                .isAnnotationPresent(org.springframework.context.annotation.Bean.class),
                "restTemplate method should be annotated with @Bean");
    }
}
