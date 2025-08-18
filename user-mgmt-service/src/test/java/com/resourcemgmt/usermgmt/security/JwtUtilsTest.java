package com.resourcemgmt.usermgmt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @Mock
    private UserDetails userDetails;

    private final String secretKey = "mySecretKey12345678901234567890123456789012"; // 32 bytes for 256-bit key
    private final int expirationMs = 86400000; // 24 hours in milliseconds
    private final String username = "testuser";
    private String validToken;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // Set private fields using ReflectionTestUtils
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", secretKey);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", expirationMs);
        
        // Generate a valid token for testing
        validToken = jwtUtils.generateToken(username, "SUPER_ADMIN", 1L);
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsernameFromToken() {
        // Act
        String extractedUsername = jwtUtils.extractUsername(validToken);

        // Assert
        assertEquals(username, extractedUsername);
    }

    @Test
    void extractExpiration_ShouldReturnFutureDate() {
        // Act
        Date expiration = jwtUtils.extractExpiration(validToken);

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void generateToken_ShouldCreateValidTokenWithUsername() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(username);
        
        // Act
        String token = jwtUtils.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertEquals(username, jwtUtils.extractUsername(token));
    }

    @Test
    void generateToken_ShouldCreateValidTokenWithUsernameRoleAndUserId() {
        // Arrange
        String role = "ROLE_ADMIN";
        Long userId = 123L;

        // Act
        String token = jwtUtils.generateToken(username, role, userId);

        // Assert
        assertNotNull(token);
        assertEquals(username, jwtUtils.extractUsername(token));
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(username);

        // Act
        boolean isValid = jwtUtils.validateToken(validToken, userDetails);

        // Assert
        assertTrue(isValid);
    }



    @Test
    void validateToken_ShouldReturnFalseForInvalidUsername() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("differentUser");

        // Act
        boolean isValid = jwtUtils.validateToken(validToken, userDetails);

        // Assert
        assertFalse(isValid);
    }

    // Helper method to create an expired token
    private String createExpiredToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() - 1000); // 1 second in the past
        
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
}
