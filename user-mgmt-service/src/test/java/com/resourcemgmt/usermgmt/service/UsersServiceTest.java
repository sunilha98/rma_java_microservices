package com.resourcemgmt.usermgmt.service;

import com.resourcemgmt.usermgmt.entity.User;
import com.resourcemgmt.usermgmt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.resourcemgmt.usermgmt.entity.User.UserRole.SUPER_ADMIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersService usersService;

    @Test
    void getAllUsers() {
        // Arrange
        User user1 = new User();
        User user2 = new User();
        List<User> userList = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> result = usersService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getUserById() {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = usersService.getUserById(1L);

        // Assert
        assertEquals(user, result);
    }

    @Test
    void createUser() {
        // Arrange
        User user = new User();
        user.setPassword("password");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = usersService.createUser(user, "admin");

        // Assert
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("admin", result.getCreatedBy());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }



    @Test
    void deleteUser() {
        // Act
        usersService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }
}