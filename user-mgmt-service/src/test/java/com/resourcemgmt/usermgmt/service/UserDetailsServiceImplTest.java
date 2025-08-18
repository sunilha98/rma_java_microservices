package com.resourcemgmt.usermgmt.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.resourcemgmt.usermgmt.entity.User;
import com.resourcemgmt.usermgmt.entity.User.UserRole;
import com.resourcemgmt.usermgmt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setRole(UserRole.RESOURCE);
        testUser.setIsActive(true);
    }

    @Test
    void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        // Arrange
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(testUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        
        // Verify authorities
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_RESOURCE")));
        
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_WhenUserDoesNotExist_ShouldThrowUsernameNotFoundException() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername(username)
        );
        
        assertEquals("User not found: nonexistentuser", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_WhenUserIsInactive_ShouldReturnDisabledUserDetails() {
        // Arrange
        User inactiveUser = new User();
        inactiveUser.setId(2L);
        inactiveUser.setUsername("inactiveuser");
        inactiveUser.setEmail("inactive@example.com");
        inactiveUser.setPassword("password123");
        inactiveUser.setFirstName("Inactive");
        inactiveUser.setLastName("User");
        inactiveUser.setRole(UserRole.RESOURCE);
        inactiveUser.setIsActive(false);
        
        String username = "inactiveuser";
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(inactiveUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals("inactiveuser", userDetails.getUsername());
        assertFalse(userDetails.isEnabled());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_WhenUserHasSuperAdminRole_ShouldReturnCorrectAuthorities() {
        // Arrange
        testUser.setRole(UserRole.SUPER_ADMIN);
        String username = "superadmin";
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(testUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_WhenUserHasProjectManagerRole_ShouldReturnCorrectAuthorities() {
        // Arrange
        testUser.setRole(UserRole.PROJECT_MANAGER);
        String username = "projectmanager";
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(testUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_PROJECT_MANAGER")));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_WhenUserHasFinanceControllerRole_ShouldReturnCorrectAuthorities() {
        // Arrange
        testUser.setRole(UserRole.FINANCE_CONTROLLER);
        String username = "financecontroller";
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(testUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_FINANCE_CONTROLLER")));
        verify(userRepository, times(1)).findByUsername(username);
    }
}
