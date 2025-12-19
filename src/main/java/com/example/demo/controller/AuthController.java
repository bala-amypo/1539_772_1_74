package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.security.JwtUtil;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {

        User createdUser = userService.registerUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User request) {

        User user = userService.findByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
