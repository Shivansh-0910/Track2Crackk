package com.trackdsa.controller;

import com.trackdsa.service.AIRecommendationService;
import com.trackdsa.service.UserService;
import com.trackdsa.service.GenAIRerankerService;
import com.trackdsa.model.User;
import com.trackdsa.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:3000"})
public class RecommendationController {

    @Autowired
    private AIRecommendationService recommendationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private GenAIRerankerService genAIRerankerService;

    @GetMapping("/personalized")
    public ResponseEntity<?> getPersonalizedRecommendations(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "daily") String type,
            @RequestParam(name = "mode", defaultValue = "rule") String mode) {
        
        try {
            // Extract user ID from JWT token
            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body("Invalid token");
            }
            
            String email = jwtUtil.getEmailFromToken(token);
            
            // Get user by email
            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
            
            String userId = userOpt.get().getId();
            
            List<AIRecommendationService.RecommendationResult> recommendations;
            boolean genaiAttempted = false;
            
            switch (type.toLowerCase()) {
                case "daily":
                    recommendations = recommendationService.getPersonalizedRecommendations(userId, limit);
                    break;
                case "weekly":
                    recommendations = getWeeklyRecommendations(userId, limit);
                    break;
                case "skill-gap":
                    recommendations = getSkillGapRecommendations(userId, limit);
                    break;
                default:
                    recommendations = recommendationService.getPersonalizedRecommendations(userId, limit);
            }

            // Optional GenAI rerank step
            List<String> beforeIds = recommendations.stream()
                    .map(r -> r.getProblem().getId())
                    .collect(Collectors.toList());

            if ("genai".equalsIgnoreCase(mode)) {
                try {
                    genaiAttempted = true;
                    // Need user details for context
                    User user = userOpt.get();
                    recommendations = genAIRerankerService.rerankWithGenAI(user, recommendations, limit);
                } catch (Exception ignored) {
                    // Fall back silently
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("recommendations", formatRecommendations(recommendations));
            response.put("type", type);
            response.put("generatedAt", new Date());
            response.put("totalCount", recommendations.size());
            List<String> afterIds = recommendations.stream()
                    .map(r -> r.getProblem().getId())
                    .collect(Collectors.toList());
            boolean genaiApplied = genaiAttempted && !beforeIds.equals(afterIds);
            response.put("genaiAttempted", genaiAttempted);
            response.put("genaiApplied", genaiApplied);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating recommendations: " + e.getMessage());
        }
    }

    @GetMapping("/topic/{topicName}")
    public ResponseEntity<?> getTopicSpecificRecommendations(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String topicName,
            @RequestParam(defaultValue = "5") int limit) {
        
        try {
            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body("Invalid token");
            }
            
            String email = jwtUtil.getEmailFromToken(token);
            
            // Get user by email
            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
            
            String userId = userOpt.get().getId();
            
            // Get topic-specific recommendations
            List<AIRecommendationService.RecommendationResult> recommendations = 
                getTopicRecommendations(userId, topicName, limit);

            Map<String, Object> response = new HashMap<>();
            response.put("recommendations", formatRecommendations(recommendations));
            response.put("topic", topicName);
            response.put("generatedAt", new Date());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating topic recommendations: " + e.getMessage());
        }
    }

    @GetMapping("/company/{companyName}")
    public ResponseEntity<?> getCompanySpecificRecommendations(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String companyName,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body("Invalid token");
            }
            
            String email = jwtUtil.getEmailFromToken(token);
            
            // Get user by email
            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
            
            String userId = userOpt.get().getId();
            
            // Get company-specific recommendations
            List<AIRecommendationService.RecommendationResult> recommendations = 
                getCompanyRecommendations(userId, companyName, limit);

            Map<String, Object> response = new HashMap<>();
            response.put("recommendations", formatRecommendations(recommendations));
            response.put("company", companyName);
            response.put("generatedAt", new Date());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating company recommendations: " + e.getMessage());
        }
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> submitRecommendationFeedback(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> feedbackData) {
        
        try {
            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body("Invalid token");
            }
            
            String email = jwtUtil.getEmailFromToken(token);
            
            // Get user by email
            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
            
            String userId = userOpt.get().getId();
            
            // Process feedback for ML model improvement
            processFeedback(userId, feedbackData);
            
            return ResponseEntity.ok(Map.of("message", "Feedback received successfully"));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing feedback: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testRecommendations() {
        try {
            Map<String, Object> testResponse = new HashMap<>();
            testResponse.put("message", "Recommendation service is working");
            testResponse.put("timestamp", new Date());
            testResponse.put("totalProblems", recommendationService.getTotalProblemsCount());
            return ResponseEntity.ok(testResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error testing recommendations: " + e.getMessage());
        }
    }

    @GetMapping("/insights")
    public ResponseEntity<?> getRecommendationInsights(
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body("Invalid token");
            }
            
            String email = jwtUtil.getEmailFromToken(token);
            
            // Get user by email
            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
            
            String userId = userOpt.get().getId();
            
            Map<String, Object> insights = generateInsights(userId);
            
            return ResponseEntity.ok(insights);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating insights: " + e.getMessage());
        }
    }

    // Helper methods

    private List<AIRecommendationService.RecommendationResult> getWeeklyRecommendations(String userId, int limit) {
        // Get recommendations optimized for weekly planning
        return recommendationService.getPersonalizedRecommendations(userId, limit * 2);
    }

    private List<AIRecommendationService.RecommendationResult> getSkillGapRecommendations(String userId, int limit) {
        // Focus on skill gaps
        return recommendationService.getPersonalizedRecommendations(userId, limit);
    }

    private List<AIRecommendationService.RecommendationResult> getTopicRecommendations(String userId, String topic, int limit) {
        // Topic-specific logic would go here
        return recommendationService.getPersonalizedRecommendations(userId, limit);
    }

    private List<AIRecommendationService.RecommendationResult> getCompanyRecommendations(String userId, String company, int limit) {
        // Company-specific logic would go here
        return recommendationService.getPersonalizedRecommendations(userId, limit);
    }

    private List<Map<String, Object>> formatRecommendations(List<AIRecommendationService.RecommendationResult> recommendations) {
        return recommendations.stream().map(rec -> {
            Map<String, Object> formatted = new HashMap<>();
            formatted.put("problem", rec.getProblem());
            formatted.put("score", Math.round(rec.getScore() * 100) / 100.0);
            formatted.put("reasoning", rec.getReasoning());
            formatted.put("priority", getPriorityLevel(rec.getScore()));
            formatted.put("estimatedTime", rec.getProblem().getAverageTime());
            return formatted;
        }).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    private String getPriorityLevel(double score) {
        if (score >= 0.8) return "HIGH";
        if (score >= 0.6) return "MEDIUM";
        return "LOW";
    }

    private void processFeedback(String userId, Map<String, Object> feedbackData) {
        // Store feedback for ML model training
        // This would involve saving to a feedback collection
        System.out.println("Processing feedback for user: " + userId);
        System.out.println("Feedback data: " + feedbackData);
    }

    private Map<String, Object> generateInsights(String userId) {
        Map<String, Object> insights = new HashMap<>();
        
        // Generate personalized insights
        insights.put("learningPatterns", Arrays.asList(
            "You perform better on array problems in the morning",
            "Dynamic programming problems take you 20% longer than average",
            "You have a 85% success rate on medium difficulty problems"
        ));
        
        insights.put("recommendations", Arrays.asList(
            "Focus on tree traversal algorithms this week",
            "Review sliding window technique",
            "Practice more graph problems for Google preparation"
        ));
        
        insights.put("nextMilestone", Map.of(
            "target", "Complete 50 medium problems",
            "progress", 35,
            "estimatedDays", 12
        ));
        
        return insights;
    }
}
