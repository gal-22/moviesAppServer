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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtils;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtils) {
        this.userRepository        = userRepository;
        this.passwordEncoder       = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils              = jwtUtils;
    }

    /**
     * Registers a new user, immediately authenticates them,
     * and returns a map containing both a success message and a JWT.
     */
    public Map<String, String> registerUser(User user) {
        Map<String, String> response = new HashMap<>();

        // prevent duplicate usernames
        if (userRepository.existsByUsername(user.getUsername())) {
            response.put("message", "Error: Username is already taken!");
            return response;
        }

        // encode & save
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of("USER"));
        }
        User savedUser = userRepository.save(user);

        // authenticate so we can mint a token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        savedUser.getUsername(),
                        rawPassword
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // generate JWT
        String jwt = jwtUtils.generateJwtToken(authentication.getName());

        // build response
        response.put("message",  "User registered successfully!");
        response.put("token",    jwt);
        response.put("userId",   String.valueOf(savedUser.getId()));
        response.put("username", savedUser.getUsername());
        response.put("email",    savedUser.getEmail());

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
        String jwtToken = jwtUtils.generateJwtToken(username);

        response.put("token", jwtToken);
        response.put("user", user.get());

        return response;
    }
}
