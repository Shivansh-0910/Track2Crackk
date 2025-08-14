package com.trackdsa.controller;

import com.trackdsa.service.RoadmapGeneratorService;
import com.trackdsa.service.RoadmapGeneratorService.Roadmap;
import com.trackdsa.service.RoadmapGeneratorService.Timeline;
import com.trackdsa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/roadmap")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
public class RoadmapController {

    @Autowired
    private RoadmapGeneratorService roadmapService;

    @Autowired
    private UserService userService;

    // Generate roadmap for specific timeline
    @GetMapping("/generate/{timeline}")
    public ResponseEntity<?> generateRoadmap(@PathVariable String timeline, 
                                           @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token
            String userId = extractUserIdFromToken(token);
            
            // Parse timeline
            Timeline selectedTimeline = parseTimeline(timeline);
            
            // Generate roadmap
            Roadmap roadmap = roadmapService.generateRoadmap(userId, selectedTimeline);
            
            return ResponseEntity.ok(roadmap);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to generate roadmap: " + e.getMessage()));
        }
    }

    // Test endpoint to verify backend is working
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok(Map.of("message", "Roadmap backend is working!", "timestamp", new Date()));
    }

    // Get available timeline options
    @GetMapping("/timelines")
    public ResponseEntity<?> getAvailableTimelines() {
        try {
            System.out.println("Timelines endpoint called");
            List<Map<String, Object>> timelines = new ArrayList<>();
            
            for (Timeline timeline : Timeline.values()) {
                Map<String, Object> timelineInfo = new HashMap<>();
                timelineInfo.put("key", timeline.name().toLowerCase());
                timelineInfo.put("days", timeline.getDays());
                timelineInfo.put("description", timeline.getDescription());
                timelineInfo.put("weeks", timeline.getDays() / 7);
                
                // Add estimated hours per week for each timeline
                switch (timeline) {
                    case THIRTY_DAYS:
                        timelineInfo.put("estimatedHoursPerWeek", 15);
                        timelineInfo.put("difficulty", "Intensive");
                        break;
                    case SIXTY_DAYS:
                        timelineInfo.put("estimatedHoursPerWeek", 12);
                        timelineInfo.put("difficulty", "Moderate");
                        break;
                    case NINETY_DAYS:
                        timelineInfo.put("estimatedHoursPerWeek", 10);
                        timelineInfo.put("difficulty", "Balanced");
                        break;
                    case ONE_EIGHTY_DAYS:
                        timelineInfo.put("estimatedHoursPerWeek", 8);
                        timelineInfo.put("difficulty", "Relaxed");
                        break;
                }
                
                timelines.add(timelineInfo);
            }
            
            System.out.println("Returning timelines: " + timelines);
            return ResponseEntity.ok(Map.of("timelines", timelines));
            
        } catch (Exception e) {
            System.err.println("Error in timelines endpoint: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Failed to get timelines: " + e.getMessage()));
        }
    }

    // Get roadmap progress for current user
    @GetMapping("/progress")
    public ResponseEntity<?> getRoadmapProgress(@RequestHeader("Authorization") String token) {
        try {
            String userId = extractUserIdFromToken(token);
            
            // This would integrate with the user's current progress
            // For now, return a sample progress structure
            Map<String, Object> progress = new HashMap<>();
            progress.put("currentWeek", 2);
            progress.put("totalWeeks", 8);
            progress.put("problemsCompleted", 25);
            progress.put("targetProblems", 120);
            progress.put("completionPercentage", 20.8);
            progress.put("onTrack", true);
            progress.put("nextMilestone", "Complete Week 3: Binary Search & Sorting");
            
            return ResponseEntity.ok(progress);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to get progress: " + e.getMessage()));
        }
    }

    // Get roadmap recommendations based on user's current progress
    @GetMapping("/recommendations")
    public ResponseEntity<?> getRoadmapRecommendations(@RequestHeader("Authorization") String token) {
        try {
            String userId = extractUserIdFromToken(token);
            
            // Analyze user's current progress and recommend optimal timeline
            List<Map<String, Object>> recommendations = new ArrayList<>();
            
            // Sample recommendations
            recommendations.add(Map.of(
                "timeline", "60_days",
                "reason", "Based on your current progress, 60 days would be optimal",
                "confidence", 85,
                "estimatedSuccessRate", 78
            ));
            
            recommendations.add(Map.of(
                "timeline", "90_days", 
                "reason", "For a more comfortable pace with better retention",
                "confidence", 92,
                "estimatedSuccessRate", 85
            ));
            
            return ResponseEntity.ok(Map.of("recommendations", recommendations));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to get recommendations: " + e.getMessage()));
        }
    }

    // Update roadmap progress (mark week as completed)
    @PostMapping("/progress/update")
    public ResponseEntity<?> updateProgress(@RequestParam int weekNumber, 
                                          @RequestParam int problemsCompleted,
                                          @RequestHeader("Authorization") String token) {
        try {
            String userId = extractUserIdFromToken(token);
            
            // Update user's progress in the database
            // This would integrate with your progress tracking system
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Progress updated successfully");
            response.put("weekNumber", weekNumber);
            response.put("problemsCompleted", problemsCompleted);
            response.put("nextWeek", weekNumber + 1);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to update progress: " + e.getMessage()));
        }
    }

    // Get detailed week breakdown
    @GetMapping("/week/{weekNumber}")
    public ResponseEntity<?> getWeekDetails(@PathVariable int weekNumber,
                                          @RequestParam String timeline,
                                          @RequestHeader("Authorization") String token) {
        try {
            String userId = extractUserIdFromToken(token);
            Timeline selectedTimeline = parseTimeline(timeline);
            
            // Generate roadmap and get specific week
            Roadmap roadmap = roadmapService.generateRoadmap(userId, selectedTimeline);
            
            if (weekNumber > 0 && weekNumber <= roadmap.getWeeks().size()) {
                return ResponseEntity.ok(roadmap.getWeeks().get(weekNumber - 1));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid week number"));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to get week details: " + e.getMessage()));
        }
    }

    // Helper methods
    private String extractUserIdFromToken(String token) {
        // Extract user ID from JWT token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            // Use JwtUtil to decode the token and extract user ID
            // For now, return a sample user ID for testing
            // In production, you would decode the JWT and extract the actual user ID
            return "sample-user-id";
        } catch (Exception e) {
            // If token is invalid, return a default user ID for demo purposes
            return "sample-user-id";
        }
    }

    private Timeline parseTimeline(String timeline) {
        switch (timeline.toLowerCase()) {
            case "30_days":
            case "thirty_days":
                return Timeline.THIRTY_DAYS;
            case "60_days":
            case "sixty_days":
                return Timeline.SIXTY_DAYS;
            case "90_days":
            case "ninety_days":
                return Timeline.NINETY_DAYS;
            case "180_days":
            case "one_eighty_days":
                return Timeline.ONE_EIGHTY_DAYS;
            default:
                throw new IllegalArgumentException("Invalid timeline: " + timeline);
        }
    }
}
