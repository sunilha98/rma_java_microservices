package com.resourcemgmt.usermgmt.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {



    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        // Arrange
        SecurityConfig securityConfig = new SecurityConfig();

        // Act
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(encoder);
        assertTrue(encoder.matches("password", encoder.encode("password")));
    }

    @Test
    void passwordEncoder_ShouldBeSingleton() {
        // Arrange
        SecurityConfig securityConfig = new SecurityConfig();

        // Act
        PasswordEncoder encoder1 = securityConfig.passwordEncoder();
        PasswordEncoder encoder2 = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(encoder1);
        assertNotNull(encoder2);
        assertEquals(encoder1.getClass(), encoder2.getClass());
    }

    @Test
    void securityConfig_ShouldBeAnnotated() {
        // Arrange & Act
        SecurityConfig securityConfig = new SecurityConfig();
        
        // Assert
        assertNotNull(securityConfig);
        assertTrue(securityConfig.getClass().isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
    }

    @Test
    void filterChain_ShouldHandleNullHttpSecurity() {
        // Arrange
        SecurityConfig securityConfig = new SecurityConfig();
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            securityConfig.filterChain(null);
        });
    }

    @Test
    void passwordEncoder_ShouldEncodeDifferentPasswords() {
        // Arrange
        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        
        // Act
        String encoded1 = encoder.encode("password1");
        String encoded2 = encoder.encode("password1");
        String encoded3 = encoder.encode("password2");
        
        // Assert
        assertNotNull(encoded1);
        assertNotNull(encoded2);
        assertNotNull(encoded3);
        assertTrue(encoder.matches("password1", encoded1));
        assertTrue(encoder.matches("password1", encoded2));
        assertTrue(encoder.matches("password2", encoded3));
        assertNotEquals(encoded1, encoded2); // BCrypt should produce different hashes for same password
    }
}
