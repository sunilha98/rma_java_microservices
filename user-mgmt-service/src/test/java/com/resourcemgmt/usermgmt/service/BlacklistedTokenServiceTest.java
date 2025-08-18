package com.resourcemgmt.usermgmt.service;

import com.resourcemgmt.usermgmt.entity.BlacklistedToken;
import com.resourcemgmt.usermgmt.repository.BlacklistedTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlacklistedTokenServiceTest {

    @Mock
    private BlacklistedTokenRepository repository;

    @InjectMocks
    private BlacklistedTokenService blacklistedTokenService;

    private BlacklistedToken blacklistedToken;

    @BeforeEach
    void setUp() {
        blacklistedToken = new BlacklistedToken();
        blacklistedToken.setId(1L);
        blacklistedToken.setToken("test-token-123");
    }

    @Test
    void addToken_ShouldSaveAndReturnBlacklistedToken() {
        // Arrange
        String token = "test-token-123";
        when(repository.save(any(BlacklistedToken.class))).thenReturn(blacklistedToken);

        // Act
        BlacklistedToken result = blacklistedTokenService.addToken(token);

        // Assert
        assertNotNull(result);
        assertEquals("test-token-123", result.getToken());
        assertEquals(1L, result.getId());
        
        verify(repository, times(1)).save(any(BlacklistedToken.class));
    }

    @Test
    void addToken_ShouldCreateNewBlacklistedTokenWithGivenToken() {
        // Arrange
        String token = "another-test-token-456";
        BlacklistedToken expectedToken = new BlacklistedToken();
        expectedToken.setId(2L);
        expectedToken.setToken(token);
        
        when(repository.save(any(BlacklistedToken.class))).thenReturn(expectedToken);

        // Act
        BlacklistedToken result = blacklistedTokenService.addToken(token);

        // Assert
        assertNotNull(result);
        assertEquals(token, result.getToken());
        
        // Verify that repository.save was called with a BlacklistedToken having the correct token
        verify(repository).save(argThat(blacklistedToken -> 
            blacklistedToken.getToken().equals(token)));
    }

    @Test
    void addToken_ShouldHandleEmptyToken() {
        // Arrange
        String emptyToken = "";
        BlacklistedToken expectedToken = new BlacklistedToken();
        expectedToken.setId(3L);
        expectedToken.setToken("");
        
        when(repository.save(any(BlacklistedToken.class))).thenReturn(expectedToken);

        // Act
        BlacklistedToken result = blacklistedTokenService.addToken(emptyToken);

        // Assert
        assertNotNull(result);
        assertEquals("", result.getToken());
        verify(repository, times(1)).save(any(BlacklistedToken.class));
    }

    @Test
    void addToken_ShouldHandleNullToken() {
        // Arrange
        BlacklistedToken expectedToken = new BlacklistedToken();
        expectedToken.setId(4L);
        expectedToken.setToken(null);
        
        when(repository.save(any(BlacklistedToken.class))).thenReturn(expectedToken);

        // Act
        BlacklistedToken result = blacklistedTokenService.addToken(null);

        // Assert
        assertNotNull(result);
        assertNull(result.getToken());
        verify(repository, times(1)).save(any(BlacklistedToken.class));
    }
}
