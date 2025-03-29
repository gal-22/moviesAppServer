package com.moviesapp.moviesapp.controllers;

import org.springframework.web.bind.annotation.*;

import com.moviesapp.moviesapp.models.User;
import com.moviesapp.moviesapp.services.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * ✅ Register a new user
     */
    @PostMapping("/register")
    public Map<String, String> registerUser(@RequestBody User user) {
        return authService.registerUser(user);
    }

    /**
     * ✅ Login user and return JWT token
     */
    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody Map<String, String> loginRequest) {
        return authService.loginUser(loginRequest);
    }
}
