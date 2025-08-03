package com.trackdsa.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
public class AnalyticsController {

    @GetMapping
    public ResponseEntity<?> getAnalytics() {
        // Mock data for now - replace with actual service calls
        Map<String, Object> analytics = Map.of(
            "totalProblemsSolved", 25,
            "streakDays", 7,
            "weeklyGoal", 10,
            "weeklyProgress", 6,
            "topicMastery", Arrays.asList(
                Map.of("topic", "Arrays", "mastery", 85),
                Map.of("topic", "Strings", "mastery", 72),
                Map.of("topic", "Linked Lists", "mastery", 68),
                Map.of("topic", "Trees", "mastery", 45),
                Map.of("topic", "Dynamic Programming", "mastery", 35)
            ),
            "companyProgress", Arrays.asList(
                Map.of("company", "Google", "progress", 75),
                Map.of("company", "Amazon", "progress", 68),
                Map.of("company", "Microsoft", "progress", 82),
                Map.of("company", "Meta", "progress", 55),
                Map.of("company", "Apple", "progress", 60)
            ),
            "monthlyActivity", Arrays.asList(
                Map.of("date", "2024-01-15", "problems", 2),
                Map.of("date", "2024-01-14", "problems", 1),
                Map.of("date", "2024-01-13", "problems", 3),
                Map.of("date", "2024-01-12", "problems", 2),
                Map.of("date", "2024-01-11", "problems", 4)
            ),
            "difficultyDistribution", Map.of(
                "easy", 15,
                "medium", 8,
                "hard", 2
            )
        );

        return ResponseEntity.ok(analytics);
    }
} 