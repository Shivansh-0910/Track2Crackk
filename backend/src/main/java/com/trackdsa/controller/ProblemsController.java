package com.trackdsa.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Date;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import com.trackdsa.model.User;
import com.trackdsa.model.UserProblem;
import com.trackdsa.repository.UserRepository;
import com.trackdsa.repository.UserProblemRepository;
import com.trackdsa.util.JwtUtil;

@RestController
@RequestMapping("/api/problems")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
public class ProblemsController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProblemRepository userProblemRepository;

    @GetMapping("/daily-recommendations")
    public ResponseEntity<?> getDailyRecommendations() {
        // Mock data for now - replace with actual service calls
        List<Map<String, Object>> dailyProblems = Arrays.asList(
            Map.of(
                "_id", "507f1f77bcf86cd799439022",
                "name", "Two Sum",
                "difficulty", "Easy",
                "topic", "Arrays",
                "url", "https://leetcode.com/problems/two-sum/",
                "estimatedTime", 15
            ),
            Map.of(
                "_id", "507f1f77bcf86cd799439023",
                "name", "Valid Parentheses",
                "difficulty", "Easy",
                "topic", "Stack",
                "url", "https://leetcode.com/problems/valid-parentheses/",
                "estimatedTime", 20
            ),
            Map.of(
                "_id", "507f1f77bcf86cd799439024",
                "name", "Merge Two Sorted Lists",
                "difficulty", "Easy",
                "topic", "Linked Lists",
                "url", "https://leetcode.com/problems/merge-two-sorted-lists/",
                "estimatedTime", 25
            )
        );

        Map<String, Object> response = Map.of("problems", dailyProblems);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{problemId}/status")
    public ResponseEntity<?> updateProblemStatus(
            @PathVariable String problemId,
            @RequestBody Map<String, String> payload,
            @RequestHeader("Authorization") String authHeader) {
        
        System.out.println("Received status update request for problem: " + problemId + " with status: " + payload.get("status"));
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }
        
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        
        String status = payload.get("status");
        String email = jwtUtil.getEmailFromToken(token);
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        
        String userId = userOpt.get().getId();
        
        // Find existing record or create new one
        Optional<UserProblem> existingRecord = userProblemRepository.findByUserIdAndProblemId(userId, problemId);
        UserProblem userProblem;
        
        if (existingRecord.isPresent()) {
            userProblem = existingRecord.get();
            userProblem.setStatus(status);
            userProblem.setUpdatedAt(new Date());
        } else {
            userProblem = new UserProblem(userId, problemId, status);
        }
        
        userProblemRepository.save(userProblem);
        
        Map<String, Object> response = Map.of(
            "success", true,
            "message", "Problem status updated successfully",
            "problemId", problemId,
            "status", status
        );
        
        return ResponseEntity.ok(response);
    }
} 
