package com.resourcemgmt.resourceallocations.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private UserDetails userDetails;

    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long EXPIRATION_MS = 86400000; // 24 hours

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to set private fields
        setPrivateField(jwtUtils, "jwtSecret", SECRET);
        setPrivateField(jwtUtils, "jwtExpirationMs", (int) EXPIRATION_MS);

        when(userDetails.getUsername()).thenReturn("testuser");
    }

    private void setPrivateField(Object target, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testGenerateAndValidateToken_WithUserDetails() {
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(jwtUtils.validateToken(token, userDetails));
    }



    @Test
    void testValidateToken_WithInvalidUser() {
        String token = jwtUtils.generateToken(userDetails);
        UserDetails otherUser = mock(UserDetails.class);
        when(otherUser.getUsername()).thenReturn("otheruser");

        assertFalse(jwtUtils.validateToken(token, otherUser));
    }



    @Test
    void testTokenValidation_WithTamperedToken() {
        String validToken = jwtUtils.generateToken(userDetails);
        String tamperedToken = validToken.substring(0, validToken.length() - 2) + "xx";

        assertThrows(Exception.class, () -> jwtUtils.validateToken(tamperedToken, userDetails));
    }
}