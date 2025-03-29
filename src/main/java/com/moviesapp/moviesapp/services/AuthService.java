package com.moviesapp.moviesapp.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.moviesapp.moviesapp.models.User;
import com.moviesapp.moviesapp.repositories.UserRepository;
import com.moviesapp.moviesapp.utils.JwtUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, String> registerUser(User user) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.existsByUsername(user.getUsername())) {
            response.put("message", "Error: Username is already taken!");
            return response;
        }

        // Encrypt the plain-text password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Set the default role if not already provided
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of("USER"));
        }

        userRepository.save(user);
        response.put("message", "User registered successfully!");
        return response;
    }

    public Map<String, Object> loginUser(Map<String, String> loginRequest) {
        Map<String, Object> response = new HashMap<>();

        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            response.put("message", "Error: Invalid username or password");
            return response;
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtil.generateJwtToken(username);

        response.put("token", jwtToken);
        response.put("user", user.get());

        return response;
    }
}
