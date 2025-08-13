package com.resourcemgmt.usermgmt.controller;

import com.resourcemgmt.usermgmt.activities.ActivityContextHolder;
import com.resourcemgmt.usermgmt.activities.LogActivity;
import com.resourcemgmt.usermgmt.entity.User;
import com.resourcemgmt.usermgmt.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    public static String TOKEN;

    @Autowired
    private UsersService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @LogActivity(action = "Created user", module = "User Management")
    public User createUser(@RequestBody User user, @RequestHeader("X-Auth-Username") String createdBy, @RequestHeader("X-Bearer-Token") String token) {

        User resUser = userService.createUser(user, createdBy);

        TOKEN = token;
        ActivityContextHolder.setDetail("User", resUser.getFirstName() + " " + resUser.getLastName());

        return resUser;
    }

    @PutMapping("/{id}")
    @LogActivity(action = "Updated user", module = "User Management")
    public User updateUser(@PathVariable Long id, @RequestBody User user, @RequestHeader("X-Auth-Username") String updatedBy, @RequestHeader("X-Bearer-Token") String token) {

        User resUser = userService.updateUser(id, user, updatedBy);

        TOKEN = token;
        ActivityContextHolder.setDetail("User", resUser.getFirstName() + " " + resUser.getLastName());

        return resUser;
    }

    @DeleteMapping("/{id}")
    @LogActivity(action = "Deleted user", module = "User Management")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @RequestHeader("X-Bearer-Token") String token) {
        userService.deleteUser(id);

        TOKEN = token;
        ActivityContextHolder.setDetail("User Id", id.toString());
        return ResponseEntity.ok().build();
    }
}
