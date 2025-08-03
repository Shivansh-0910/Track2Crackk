package com.trackdsa.controller;

import com.trackdsa.model.User;
import com.trackdsa.service.UserService;
import com.trackdsa.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        // Set default name from email if not provided
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            String emailPrefix = user.getEmail().split("@")[0];
            user.setName(emailPrefix);
        }

        User saved = userService.register(user);
        saved.setPassword(null); // Don't return password
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        return userService.findByEmail(email)
                .filter(user -> userService.checkPassword(password, user.getPassword()))
                .map(user -> {
                    String accessToken = jwtUtil.generateToken(user.getEmail());
                    String refreshToken = user.getRefreshToken();

                    Map<String, Object> response = new HashMap<>();
                    response.put("accessToken", accessToken);
                    response.put("refreshToken", refreshToken);

                    user.setPassword(null);
                    response.put("user", user);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).<Map<String, Object>>body(Map.of("error", "Invalid credentials")));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {
        String refreshToken = payload.get("refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }
        
        return userService.findByRefreshToken(refreshToken)
                .map(user -> {
                    String newAccessToken = jwtUtil.generateToken(user.getEmail());
                    String newRefreshToken = user.getRefreshToken();

                    Map<String, Object> response = new HashMap<>();
                    response.put("accessToken", newAccessToken);
                    response.put("refreshToken", newRefreshToken);

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).<Map<String, Object>>body(Map.of("error", "Invalid refresh token")));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        // In a stateless JWT setup, logout is typically handled client-side
        // by removing the token. This endpoint can be used for logging purposes
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }
} 