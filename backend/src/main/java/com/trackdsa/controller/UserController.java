package com.trackdsa.controller;

import com.trackdsa.model.User;
import com.trackdsa.repository.UserRepository;
import com.trackdsa.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        String email = jwtUtil.getEmailFromToken(token);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        User user = userOpt.get();
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Object> updates) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        String email = jwtUtil.getEmailFromToken(token);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOpt.get();

        // Update user fields based on the request body
        if (updates.containsKey("name")) {
            user.setName((String) updates.get("name"));
        }
        if (updates.containsKey("bio")) {
            user.setBio((String) updates.get("bio"));
        }
        if (updates.containsKey("level")) {
            user.setLevel((String) updates.get("level"));
        }
        if (updates.containsKey("targetCompanies")) {
            @SuppressWarnings("unchecked")
            List<String> companies = (List<String>) updates.get("targetCompanies");
            user.setTargetCompanies(companies);
        }
        if (updates.containsKey("timeAvailability")) {
            user.setTimeAvailability((String) updates.get("timeAvailability"));
        }
        if (updates.containsKey("weeklyGoal")) {
            Object weeklyGoalObj = updates.get("weeklyGoal");
            if (weeklyGoalObj instanceof Number) {
                user.setWeeklyGoal(((Number) weeklyGoalObj).intValue());
            } else if (weeklyGoalObj instanceof String) {
                try {
                    user.setWeeklyGoal(Integer.parseInt((String) weeklyGoalObj));
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("Invalid number format for weeklyGoal");
                }
            }
        }
        if (updates.containsKey("leetcode_username")) {
            user.setLeetcodeUsername((String) updates.get("leetcode_username"));
        }
        if (updates.containsKey("total_problems_solved")) {
            Object totalProblemsObj = updates.get("total_problems_solved");
            if (totalProblemsObj instanceof Number) {
                user.setTotalProblemsSolved(((Number) totalProblemsObj).intValue());
            } else if (totalProblemsObj instanceof String) {
                try {
                    user.setTotalProblemsSolved(Integer.parseInt((String) totalProblemsObj));
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("Invalid number format for total_problems_solved");
                }
            }
        }
        if (updates.containsKey("last_submission_date")) {
            String dateStr = (String) updates.get("last_submission_date");
            if (dateStr != null && !dateStr.isEmpty()) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    user.setLastSubmissionDate(formatter.parse(dateStr));
                } catch (Exception e) {
                    // Try alternative format (just date)
                    try {
                        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
                        user.setLastSubmissionDate(simpleFormatter.parse(dateStr));
                    } catch (Exception e2) {
                        return ResponseEntity.badRequest().body("Invalid date format for last_submission_date");
                    }
                }
            } else {
                user.setLastSubmissionDate(null);
            }
        }

        // Save the updated user
        User savedUser = userRepository.save(user);
        savedUser.setPassword(null);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/onboarding")
    public ResponseEntity<?> completeOnboarding(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Object> onboardingData) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        String email = jwtUtil.getEmailFromToken(token);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOpt.get();

        // Update user with onboarding data
        if (onboardingData.containsKey("name")) {
            user.setName((String) onboardingData.get("name"));
        }
        if (onboardingData.containsKey("level")) {
            user.setLevel((String) onboardingData.get("level"));
        }
        if (onboardingData.containsKey("targetCompanies")) {
            @SuppressWarnings("unchecked")
            List<String> companies = (List<String>) onboardingData.get("targetCompanies");
            user.setTargetCompanies(companies);
        }
        if (onboardingData.containsKey("timeAvailability")) {
            user.setTimeAvailability((String) onboardingData.get("timeAvailability"));
        }
        if (onboardingData.containsKey("totalProblems")) {
            Object totalProblemsObj = onboardingData.get("totalProblems");
            if (totalProblemsObj instanceof Number) {
                user.setTotalProblemsSolved(((Number) totalProblemsObj).intValue());
            } else if (totalProblemsObj instanceof String) {
                try {
                    user.setTotalProblemsSolved(Integer.parseInt((String) totalProblemsObj));
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("Invalid number format for totalProblems");
                }
            }
        }

        // Mark onboarding as completed
        user.setCompletedOnboarding(true);

        // Save the updated user
        User savedUser = userRepository.save(user);
        savedUser.setPassword(null);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Onboarding completed successfully");
        response.put("user", savedUser);

        return ResponseEntity.ok(response);
    }
}