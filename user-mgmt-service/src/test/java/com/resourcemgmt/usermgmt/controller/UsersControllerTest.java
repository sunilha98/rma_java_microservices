package com.resourcemgmt.usermgmt.controller;

import com.resourcemgmt.usermgmt.entity.User;
import com.resourcemgmt.usermgmt.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Mock
    private UsersService userService;

    @InjectMocks
    private UsersController usersController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
    }

    @Test
    void getAllUsers() {
        // Arrange
        List<User> userList = Arrays.asList(testUser);
        when(userService.getAllUsers()).thenReturn(userList);

        // Act
        List<User> result = usersController.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
    }

    @Test
    void getUserById() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);

        // Act
        User result = usersController.getUserById(1L);

        // Assert
        assertEquals(testUser, result);
    }

    @Test
    void createUser() {
        // Arrange
        when(userService.createUser(testUser, "admin")).thenReturn(testUser);

        // Act
        User result = usersController.createUser(testUser, "admin", "token");

        // Assert
        assertEquals(testUser, result);
        assertEquals("token", UsersController.TOKEN);
    }

    @Test
    void updateUser() {
        // Arrange
        when(userService.updateUser(1L, testUser, "admin")).thenReturn(testUser);

        // Act
        User result = usersController.updateUser(1L, testUser, "admin", "token");

        // Assert
        assertEquals(testUser, result);
        assertEquals("token", UsersController.TOKEN);
    }

    @Test
    void deleteUser() {
        // Act
        ResponseEntity<?> response = usersController.deleteUser(1L, "token");

        // Assert
        assertEquals(ResponseEntity.ok().build(), response);
        verify(userService, times(1)).deleteUser(1L);
        assertEquals("token", UsersController.TOKEN);
    }
}