package com.trackdsa.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.*;

@RestController
@RequestMapping("/api/topics")
@CrossOrigin(origins = "http://localhost:5173")
public class TopicsController {

    @GetMapping
    public ResponseEntity<?> getTopics() {
        // Mock data for now - replace with actual service calls
        List<Map<String, Object>> topics = Arrays.asList(
            Map.of(
                "id", "1",
                "name", "Arrays",
                "description", "Array manipulation and algorithms",
                "difficulty", "Easy",
                "problemsCount", 15,
                "completedCount", 0
            ),
            Map.of(
                "id", "2", 
                "name", "Strings",
                "description", "String manipulation and algorithms",
                "difficulty", "Easy",
                "problemsCount", 12,
                "completedCount", 0
            ),
            Map.of(
                "id", "3",
                "name", "Linked Lists", 
                "description", "Linked list data structure and algorithms",
                "difficulty", "Medium",
                "problemsCount", 8,
                "completedCount", 0
            )
        );
        
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<?> getTopicDetails(@PathVariable String topicId) {
        // Mock data for now - replace with actual service calls
        Map<String, Object> topicDetails = Map.of(
            "id", topicId,
            "name", "Arrays",
            "description", "Array manipulation and algorithms",
            "difficulty", "Easy",
            "problems", Arrays.asList(
                Map.of(
                    "id", "1",
                    "title", "Two Sum",
                    "difficulty", "Easy",
                    "status", "NOT_STARTED",
                    "url", "https://leetcode.com/problems/two-sum/"
                ),
                Map.of(
                    "id", "2", 
                    "title", "Best Time to Buy and Sell Stock",
                    "difficulty", "Easy",
                    "status", "NOT_STARTED",
                    "url", "https://leetcode.com/problems/best-time-to-buy-and-sell-stock/"
                )
            )
        );
        
        return ResponseEntity.ok(topicDetails);
    }
} 