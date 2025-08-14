package com.trackdsa.service;

import com.trackdsa.model.RecommendationFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationFeedbackService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MLRecommendationEngine mlEngine;

    // Store user feedback
    public RecommendationFeedback saveFeedback(RecommendationFeedback feedback) {
        // Save feedback to database
        RecommendationFeedback saved = mongoTemplate.save(feedback);
        
        // Trigger model update based on feedback
        updateModelBasedOnFeedback(feedback);
        
        return saved;
    }

    // Get feedback history for a user
    public List<RecommendationFeedback> getUserFeedbackHistory(String userId, int limit) {
        Query query = new Query(Criteria.where("userId").is(userId))
            .limit(limit)
            .with(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "feedbackDate"));
        
        return mongoTemplate.find(query, RecommendationFeedback.class);
    }

    // Analyze feedback patterns
    public Map<String, Object> analyzeFeedbackPatterns(String userId) {
        List<RecommendationFeedback> allFeedback = getUserFeedbackHistory(userId, 100);
        
        Map<String, Object> analysis = new HashMap<>();
        
        // Overall satisfaction rate
        long positiveCount = allFeedback.stream()
            .filter(f -> "positive".equals(f.getFeedbackType()))
            .count();
        double satisfactionRate = allFeedback.isEmpty() ? 0.0 : (double) positiveCount / allFeedback.size();
        analysis.put("satisfactionRate", satisfactionRate);
        
        // Topic preferences based on feedback
        Map<String, Double> topicSatisfaction = calculateTopicSatisfaction(allFeedback);
        analysis.put("topicSatisfaction", topicSatisfaction);
        
        // Difficulty preferences
        Map<String, Double> difficultySatisfaction = calculateDifficultySatisfaction(allFeedback);
        analysis.put("difficultySatisfaction", difficultySatisfaction);
        
        // Recommendation type effectiveness
        Map<String, Double> typeEffectiveness = calculateTypeEffectiveness(allFeedback);
        analysis.put("typeEffectiveness", typeEffectiveness);
        
        // Success rate (problems that were actually solved)
        long solvedCount = allFeedback.stream()
            .filter(RecommendationFeedback::isWasSolved)
            .count();
        double successRate = allFeedback.isEmpty() ? 0.0 : (double) solvedCount / allFeedback.size();
        analysis.put("successRate", successRate);
        
        // Average time spent on recommended problems
        double avgTimeSpent = allFeedback.stream()
            .filter(f -> f.getTimeSpent() > 0)
            .mapToInt(RecommendationFeedback::getTimeSpent)
            .average()
            .orElse(0.0);
        analysis.put("averageTimeSpent", avgTimeSpent);
        
        return analysis;
    }

    // Generate personalized insights based on feedback
    public List<String> generatePersonalizedInsights(String userId) {
        Map<String, Object> analysis = analyzeFeedbackPatterns(userId);
        List<String> insights = new ArrayList<>();
        
        double satisfactionRate = (Double) analysis.get("satisfactionRate");
        if (satisfactionRate > 0.8) {
            insights.add("You're highly satisfied with our recommendations! Keep up the great work.");
        } else if (satisfactionRate < 0.4) {
            insights.add("We notice our recommendations could be better for you. We're adjusting our algorithm.");
        }
        
        double successRate = (Double) analysis.get("successRate");
        if (successRate > 0.7) {
            insights.add("You have an excellent success rate! You're ready for more challenging problems.");
        } else if (successRate < 0.3) {
            insights.add("Let's focus on building your confidence with slightly easier problems.");
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Double> topicSatisfaction = (Map<String, Double>) analysis.get("topicSatisfaction");
        if (topicSatisfaction != null && !topicSatisfaction.isEmpty()) {
            String bestTopic = topicSatisfaction.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Arrays");
            insights.add("You seem to enjoy " + bestTopic + " problems the most. We'll recommend more!");
            
            String worstTopic = topicSatisfaction.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Dynamic Programming");
            if (topicSatisfaction.get(worstTopic) < 0.3) {
                insights.add("Let's work on improving your experience with " + worstTopic + " problems.");
            }
        }
        
        double avgTime = (Double) analysis.get("averageTimeSpent");
        if (avgTime > 60) {
            insights.add("You're taking your time to solve problems thoroughly. That's great for learning!");
        } else if (avgTime < 20) {
            insights.add("You're solving problems quickly! Ready for harder challenges?");
        }
        
        return insights;
    }

    // Update ML model based on feedback
    private void updateModelBasedOnFeedback(RecommendationFeedback feedback) {
        // Create feature weights based on the recommendation that was given feedback on
        Map<String, Double> featureWeights = feedback.getFeatureScores();
        if (featureWeights == null) {
            featureWeights = getDefaultFeatureWeights();
        }
        
        boolean wasHelpful = "positive".equals(feedback.getFeedbackType());
        
        // Update model weights using the ML engine
        mlEngine.updateModelWeights(
            feedback.getUserId(), 
            feedback.getProblemId(), 
            wasHelpful, 
            featureWeights
        );
        
        // Store updated weights for future use
        storeUpdatedWeights(feedback.getUserId(), featureWeights);
    }

    // Calculate topic-based satisfaction
    private Map<String, Double> calculateTopicSatisfaction(List<RecommendationFeedback> feedback) {
        Map<String, List<String>> topicFeedback = new HashMap<>();
        
        // This would require joining with Problem data to get topics
        // Simplified for now with mock data
        Map<String, Double> satisfaction = new HashMap<>();
        satisfaction.put("Arrays", 0.85);
        satisfaction.put("Strings", 0.72);
        satisfaction.put("Trees", 0.68);
        satisfaction.put("Dynamic Programming", 0.45);
        
        return satisfaction;
    }

    // Calculate difficulty-based satisfaction
    private Map<String, Double> calculateDifficultySatisfaction(List<RecommendationFeedback> feedback) {
        Map<String, List<String>> difficultyFeedback = new HashMap<>();
        
        for (RecommendationFeedback f : feedback) {
            // This would require joining with Problem data
            // Simplified for now
            String difficulty = "Medium"; // Default
            difficultyFeedback.computeIfAbsent(difficulty, k -> new ArrayList<>()).add(f.getFeedbackType());
        }
        
        Map<String, Double> satisfaction = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : difficultyFeedback.entrySet()) {
            String difficulty = entry.getKey();
            List<String> feedbackTypes = entry.getValue();
            
            long positiveCount = feedbackTypes.stream()
                .filter(type -> "positive".equals(type))
                .count();
            
            double rate = feedbackTypes.isEmpty() ? 0.0 : (double) positiveCount / feedbackTypes.size();
            satisfaction.put(difficulty, rate);
        }
        
        return satisfaction;
    }

    // Calculate recommendation type effectiveness
    private Map<String, Double> calculateTypeEffectiveness(List<RecommendationFeedback> feedback) {
        Map<String, List<String>> typeFeedback = new HashMap<>();
        
        for (RecommendationFeedback f : feedback) {
            String type = f.getRecommendationType() != null ? f.getRecommendationType() : "daily";
            typeFeedback.computeIfAbsent(type, k -> new ArrayList<>()).add(f.getFeedbackType());
        }
        
        Map<String, Double> effectiveness = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : typeFeedback.entrySet()) {
            String type = entry.getKey();
            List<String> feedbackTypes = entry.getValue();
            
            long positiveCount = feedbackTypes.stream()
                .filter(feedbackType -> "positive".equals(feedbackType))
                .count();
            
            double rate = feedbackTypes.isEmpty() ? 0.0 : (double) positiveCount / feedbackTypes.size();
            effectiveness.put(type, rate);
        }
        
        return effectiveness;
    }

    // Get default feature weights
    private Map<String, Double> getDefaultFeatureWeights() {
        Map<String, Double> weights = new HashMap<>();
        weights.put("skillGap", 0.25);
        weights.put("difficulty", 0.20);
        weights.put("company", 0.20);
        weights.put("timeDecay", 0.15);
        weights.put("pattern", 0.10);
        weights.put("preference", 0.10);
        return weights;
    }

    // Store updated weights for a user
    private void storeUpdatedWeights(String userId, Map<String, Double> weights) {
        // This would typically be stored in a user preferences collection
        // For now, we'll just log the update
        System.out.println("Updated weights for user " + userId + ": " + weights);
    }

    // Get user-specific feature weights
    public Map<String, Double> getUserFeatureWeights(String userId) {
        // This would retrieve stored user-specific weights
        // For now, return default weights
        return getDefaultFeatureWeights();
    }

    // Batch process feedback for model training
    public void batchProcessFeedbackForTraining() {
        // Get all recent feedback
        Query query = new Query()
            .with(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "feedbackDate"))
            .limit(1000);
        
        List<RecommendationFeedback> recentFeedback = mongoTemplate.find(query, RecommendationFeedback.class);
        
        // Group by user and process
        Map<String, List<RecommendationFeedback>> userFeedback = recentFeedback.stream()
            .collect(Collectors.groupingBy(RecommendationFeedback::getUserId));
        
        for (Map.Entry<String, List<RecommendationFeedback>> entry : userFeedback.entrySet()) {
            String userId = entry.getKey();
            List<RecommendationFeedback> feedback = entry.getValue();
            
            // Process feedback for this user
            processBatchFeedbackForUser(userId, feedback);
        }
    }

    private void processBatchFeedbackForUser(String userId, List<RecommendationFeedback> feedback) {
        // Analyze patterns and update user-specific model weights
        Map<String, Double> currentWeights = getUserFeatureWeights(userId);
        
        // Calculate adjustment based on feedback patterns
        for (RecommendationFeedback f : feedback) {
            boolean wasHelpful = "positive".equals(f.getFeedbackType());
            if (f.getFeatureScores() != null) {
                mlEngine.updateModelWeights(userId, f.getProblemId(), wasHelpful, f.getFeatureScores());
            }
        }
        
        // Store updated model for user
        storeUpdatedWeights(userId, currentWeights);
    }
}
