package com.resourcemgmt.usermgmt.controller;

import com.resourcemgmt.usermgmt.dto.AuthResponse;
import com.resourcemgmt.usermgmt.dto.LoginRequest;
import com.resourcemgmt.usermgmt.service.AuthService;
import com.resourcemgmt.usermgmt.service.BlacklistedTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private BlacklistedTokenService blacklistedTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.login(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("X-Bearer-Token") String token) {
        blacklistedTokenService.addToken(token);
        return ResponseEntity.ok("User logged out successfully");
    }

}