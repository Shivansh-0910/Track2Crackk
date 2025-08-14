package com.trackdsa.controller;

import com.trackdsa.service.DataSeederService;
import com.trackdsa.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Admin-only data management controller
 * These endpoints should only be accessible to administrators
 */
@RestController
@RequestMapping("/api/admin/data")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
public class DataController {

    @Autowired
    private DataSeederService dataSeederService;

    @Autowired
    private ProblemRepository problemRepository;

    @PostMapping("/seed")
    public ResponseEntity<?> seedData() {
        try {
            long existingCount = problemRepository.count();
            
            if (existingCount > 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Data already exists");
                response.put("existingProblems", existingCount);
                response.put("action", "skipped");
                return ResponseEntity.ok(response);
            }

            // Manually trigger seeding
            dataSeederService.run();
            
            long newCount = problemRepository.count();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Sample data seeded successfully");
            response.put("problemsAdded", newCount);
            response.put("action", "seeded");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error seeding data: " + e.getMessage());
            response.put("action", "failed");
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/reset")
    public ResponseEntity<?> resetData() {
        try {
            long deletedCount = problemRepository.count();
            problemRepository.deleteAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "All data reset successfully");
            response.put("deletedProblems", deletedCount);
            response.put("action", "reset");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error resetting data: " + e.getMessage());
            response.put("action", "failed");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getDataStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalProblems", problemRepository.count());
            stats.put("easyProblems", problemRepository.countByDifficultyAndIsActiveTrue("Easy"));
            stats.put("mediumProblems", problemRepository.countByDifficultyAndIsActiveTrue("Medium"));
            stats.put("hardProblems", problemRepository.countByDifficultyAndIsActiveTrue("Hard"));
            
            // Count by topics
            Map<String, Long> topicCounts = new HashMap<>();
            String[] topics = {"Arrays", "Strings", "Linked Lists", "Trees", "Dynamic Programming", 
                              "Graphs", "Hash Tables", "Stacks", "Heaps", "Queues"};
            
            for (String topic : topics) {
                topicCounts.put(topic, problemRepository.countByTopicAndIsActiveTrue(topic));
            }
            stats.put("topicBreakdown", topicCounts);
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error getting stats: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
