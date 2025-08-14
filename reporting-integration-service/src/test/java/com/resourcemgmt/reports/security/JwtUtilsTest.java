package com.resourcemgmt.reports.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();


        String secret = Base64.getEncoder().encodeToString(
                "123456789012345678901234567890123456789012345678".getBytes()
        );

        try {
            java.lang.reflect.Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
            secretField.setAccessible(true);
            secretField.set(jwtUtils, secret);

            java.lang.reflect.Field expirationField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
            expirationField.setAccessible(true);
            expirationField.set(jwtUtils, 3600000); 
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGenerateAndValidateToken() {
        UserDetails userDetails = User.withUsername("admin").password("password").roles("USER").build();

        String token = jwtUtils.generateToken(userDetails);
        assertThat(token).isNotNull();

        boolean isValid = jwtUtils.validateToken(token, userDetails);
        assertThat(isValid).isTrue();

        String username = jwtUtils.extractUsername(token);
        assertThat(username).isEqualTo("admin");
    }
}
