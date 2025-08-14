package com.trackdsa.service;

import com.trackdsa.model.Problem;
import com.trackdsa.model.User;
import com.trackdsa.model.UserProblemAttempt;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Machine Learning-enhanced recommendation engine
 * This service implements collaborative filtering and content-based filtering
 * to provide more accurate recommendations over time
 */
@Service
public class MLRecommendationEngine {

    // User-Item Collaborative Filtering
    public List<Problem> getCollaborativeRecommendations(User targetUser, List<User> allUsers, 
                                                         List<Problem> allProblems, 
                                                         List<UserProblemAttempt> allAttempts) {
        
        // Build user-problem matrix
        Map<String, Map<String, Double>> userProblemMatrix = buildUserProblemMatrix(allUsers, allAttempts);
        
        // Find similar users using cosine similarity
        List<UserSimilarity> similarUsers = findSimilarUsers(targetUser.getId(), userProblemMatrix);
        
        // Generate recommendations based on similar users' preferences
        return generateCollaborativeRecommendations(targetUser.getId(), similarUsers, userProblemMatrix, allProblems);
    }

    // Content-Based Filtering
    public List<Problem> getContentBasedRecommendations(User user, List<Problem> allProblems, 
                                                        List<UserProblemAttempt> userAttempts) {
        
        // Build user profile based on solved problems
        UserProfile userProfile = buildUserProfile(user, userAttempts, allProblems);
        
        // Score problems based on content similarity
        return allProblems.stream()
            .filter(problem -> !userProfile.getSolvedProblemIds().contains(problem.getId()))
            .map(problem -> new ProblemScore(problem, calculateContentSimilarity(userProfile, problem)))
            .sorted((p1, p2) -> Double.compare(p2.getScore(), p1.getScore()))
            .map(ProblemScore::getProblem)
            .collect(Collectors.toList());
    }

    // Hybrid Approach combining both methods
    public List<Problem> getHybridRecommendations(User user, List<User> allUsers, 
                                                 List<Problem> allProblems, 
                                                 List<UserProblemAttempt> allAttempts) {
        
        List<Problem> collaborativeRecs = getCollaborativeRecommendations(user, allUsers, allProblems, allAttempts);
        List<Problem> contentRecs = getContentBasedRecommendations(user, allProblems, allAttempts);
        
        // Combine and weight the recommendations
        Map<String, Double> combinedScores = new HashMap<>();
        
        // Weight collaborative filtering (60%)
        for (int i = 0; i < Math.min(collaborativeRecs.size(), 20); i++) {
            Problem problem = collaborativeRecs.get(i);
            double score = (20 - i) / 20.0 * 0.6; // Decreasing score
            combinedScores.put(problem.getId(), combinedScores.getOrDefault(problem.getId(), 0.0) + score);
        }
        
        // Weight content-based filtering (40%)
        for (int i = 0; i < Math.min(contentRecs.size(), 20); i++) {
            Problem problem = contentRecs.get(i);
            double score = (20 - i) / 20.0 * 0.4; // Decreasing score
            combinedScores.put(problem.getId(), combinedScores.getOrDefault(problem.getId(), 0.0) + score);
        }
        
        // Sort by combined scores
        return combinedScores.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .map(entry -> findProblemById(entry.getKey(), allProblems))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    // Learning Rate Optimization
    public void updateModelWeights(String userId, String problemId, boolean wasHelpful, 
                                  Map<String, Double> featureWeights) {
        
        // Simple gradient descent-like update
        double learningRate = 0.01;
        double error = wasHelpful ? 1.0 : -1.0;
        
        // Update weights based on feedback
        for (Map.Entry<String, Double> entry : featureWeights.entrySet()) {
            String feature = entry.getKey();
            double currentWeight = entry.getValue();
            double newWeight = currentWeight + learningRate * error;
            featureWeights.put(feature, Math.max(0.0, Math.min(1.0, newWeight))); // Clamp between 0 and 1
        }
    }

    // Advanced Pattern Recognition
    public List<String> identifyLearningPatterns(User user, List<UserProblemAttempt> userAttempts) {
        List<String> patterns = new ArrayList<>();
        
        // Analyze solving time patterns
        Map<String, List<Integer>> topicTimes = groupSolvingTimesByTopic(userAttempts);
        for (Map.Entry<String, List<Integer>> entry : topicTimes.entrySet()) {
            String topic = entry.getKey();
            List<Integer> times = entry.getValue();
            if (times.size() >= 3) {
                double avgTime = times.stream().mapToInt(Integer::intValue).average().orElse(0);
                if (avgTime < 20) {
                    patterns.add("Fast solver in " + topic);
                } else if (avgTime > 60) {
                    patterns.add("Needs more practice in " + topic);
                }
            }
        }
        
        // Analyze difficulty progression
        List<UserProblemAttempt> recentAttempts = userAttempts.stream()
            .sorted((a1, a2) -> a2.getAttemptDate().compareTo(a1.getAttemptDate()))
            .limit(10)
            .collect(Collectors.toList());
        
        long hardProblems = recentAttempts.stream()
            .filter(attempt -> "SOLVED".equals(attempt.getStatus()) && "Hard".equals(attempt.getDifficulty()))
            .count();
        
        if (hardProblems >= 3) {
            patterns.add("Ready for advanced problems");
        }
        
        // Analyze consistency
        long consistentDays = calculateConsistentDays(userAttempts);
        if (consistentDays >= 7) {
            patterns.add("Consistent learner - " + consistentDays + " day streak");
        }
        
        return patterns;
    }

    // Helper Methods
    private Map<String, Map<String, Double>> buildUserProblemMatrix(List<User> users, List<UserProblemAttempt> attempts) {
        Map<String, Map<String, Double>> matrix = new HashMap<>();
        
        for (UserProblemAttempt attempt : attempts) {
            String userId = attempt.getUserId();
            String problemId = attempt.getProblemId();
            
            // Score based on status and confidence
            double score = calculateAttemptScore(attempt);
            
            matrix.computeIfAbsent(userId, k -> new HashMap<>()).put(problemId, score);
        }
        
        return matrix;
    }
    
    private double calculateAttemptScore(UserProblemAttempt attempt) {
        double baseScore = 0.0;
        
        switch (attempt.getStatus()) {
            case "SOLVED":
                baseScore = 1.0;
                break;
            case "ATTEMPTED":
                baseScore = 0.5;
                break;
            case "REVIEWED":
                baseScore = 0.7;
                break;
            default:
                baseScore = 0.1;
        }
        
        // Adjust based on confidence level
        if (attempt.getConfidenceLevel() > 0) {
            baseScore *= (attempt.getConfidenceLevel() / 5.0);
        }
        
        // Adjust based on number of attempts (fewer attempts = higher score)
        baseScore *= Math.max(0.5, 1.0 - (attempt.getAttempts() - 1) * 0.1);
        
        return baseScore;
    }
    
    private List<UserSimilarity> findSimilarUsers(String targetUserId, Map<String, Map<String, Double>> matrix) {
        Map<String, Double> targetUserVector = matrix.get(targetUserId);
        if (targetUserVector == null) return new ArrayList<>();
        
        List<UserSimilarity> similarities = new ArrayList<>();
        
        for (Map.Entry<String, Map<String, Double>> entry : matrix.entrySet()) {
            String userId = entry.getKey();
            if (userId.equals(targetUserId)) continue;
            
            Map<String, Double> userVector = entry.getValue();
            double similarity = calculateCosineSimilarity(targetUserVector, userVector);
            
            if (similarity > 0.1) { // Minimum similarity threshold
                similarities.add(new UserSimilarity(userId, similarity));
            }
        }
        
        return similarities.stream()
            .sorted((s1, s2) -> Double.compare(s2.getSimilarity(), s1.getSimilarity()))
            .limit(10) // Top 10 similar users
            .collect(Collectors.toList());
    }
    
    private double calculateCosineSimilarity(Map<String, Double> vector1, Map<String, Double> vector2) {
        Set<String> commonKeys = new HashSet<>(vector1.keySet());
        commonKeys.retainAll(vector2.keySet());
        
        if (commonKeys.isEmpty()) return 0.0;
        
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (String key : commonKeys) {
            double val1 = vector1.get(key);
            double val2 = vector2.get(key);
            
            dotProduct += val1 * val2;
            norm1 += val1 * val1;
            norm2 += val2 * val2;
        }
        
        if (norm1 == 0.0 || norm2 == 0.0) return 0.0;
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
    
    private List<Problem> generateCollaborativeRecommendations(String targetUserId, 
                                                              List<UserSimilarity> similarUsers,
                                                              Map<String, Map<String, Double>> matrix,
                                                              List<Problem> allProblems) {
        
        Map<String, Double> problemScores = new HashMap<>();
        Map<String, Double> targetUserProblems = matrix.get(targetUserId);
        Set<String> solvedProblems = targetUserProblems != null ? targetUserProblems.keySet() : new HashSet<>();
        
        for (UserSimilarity similarity : similarUsers) {
            Map<String, Double> similarUserProblems = matrix.get(similarity.getUserId());
            if (similarUserProblems == null) continue;
            
            for (Map.Entry<String, Double> entry : similarUserProblems.entrySet()) {
                String problemId = entry.getKey();
                double problemScore = entry.getValue();
                
                // Skip problems already solved by target user
                if (solvedProblems.contains(problemId)) continue;
                
                // Weight by user similarity
                double weightedScore = problemScore * similarity.getSimilarity();
                problemScores.put(problemId, problemScores.getOrDefault(problemId, 0.0) + weightedScore);
            }
        }
        
        return problemScores.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .map(entry -> findProblemById(entry.getKey(), allProblems))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    private UserProfile buildUserProfile(User user, List<UserProblemAttempt> attempts, List<Problem> allProblems) {
        UserProfile profile = new UserProfile();
        
        // Get solved problems
        Set<String> solvedProblemIds = attempts.stream()
            .filter(attempt -> "SOLVED".equals(attempt.getStatus()))
            .map(UserProblemAttempt::getProblemId)
            .collect(Collectors.toSet());
        
        profile.setSolvedProblemIds(solvedProblemIds);
        
        // Analyze preferred topics
        Map<String, Integer> topicCounts = new HashMap<>();
        for (String problemId : solvedProblemIds) {
            Problem problem = findProblemById(problemId, allProblems);
            if (problem != null) {
                topicCounts.put(problem.getTopic(), topicCounts.getOrDefault(problem.getTopic(), 0) + 1);
            }
        }
        profile.setPreferredTopics(topicCounts);
        
        // Analyze preferred difficulty
        Map<String, Integer> difficultyCounts = new HashMap<>();
        for (String problemId : solvedProblemIds) {
            Problem problem = findProblemById(problemId, allProblems);
            if (problem != null) {
                difficultyCounts.put(problem.getDifficulty(), difficultyCounts.getOrDefault(problem.getDifficulty(), 0) + 1);
            }
        }
        profile.setPreferredDifficulties(difficultyCounts);
        
        return profile;
    }
    
    private double calculateContentSimilarity(UserProfile userProfile, Problem problem) {
        double similarity = 0.0;
        
        // Topic similarity
        String problemTopic = problem.getTopic();
        int topicCount = userProfile.getPreferredTopics().getOrDefault(problemTopic, 0);
        double topicSimilarity = Math.min(1.0, topicCount / 5.0); // Normalize to max 5 problems
        similarity += topicSimilarity * 0.5;
        
        // Difficulty similarity
        String problemDifficulty = problem.getDifficulty();
        int difficultyCount = userProfile.getPreferredDifficulties().getOrDefault(problemDifficulty, 0);
        double difficultySimilarity = Math.min(1.0, difficultyCount / 10.0); // Normalize to max 10 problems
        similarity += difficultySimilarity * 0.3;
        
        // Company relevance (if user has target companies)
        // This would require access to user's target companies
        similarity += 0.2; // Default company score
        
        return similarity;
    }
    
    private Problem findProblemById(String problemId, List<Problem> problems) {
        return problems.stream()
            .filter(problem -> problemId.equals(problem.getId()))
            .findFirst()
            .orElse(null);
    }
    
    private Map<String, List<Integer>> groupSolvingTimesByTopic(List<UserProblemAttempt> attempts) {
        Map<String, List<Integer>> topicTimes = new HashMap<>();
        
        for (UserProblemAttempt attempt : attempts) {
            if ("SOLVED".equals(attempt.getStatus()) && attempt.getTimeSpent() > 0) {
                // We'd need to get topic from problem - simplified for now
                String topic = "General"; // Default topic
                topicTimes.computeIfAbsent(topic, k -> new ArrayList<>()).add(attempt.getTimeSpent());
            }
        }
        
        return topicTimes;
    }
    
    private long calculateConsistentDays(List<UserProblemAttempt> attempts) {
        // Simplified consistency calculation
        Set<String> uniqueDays = attempts.stream()
            .map(attempt -> attempt.getAttemptDate().toString().substring(0, 10)) // Get date part
            .collect(Collectors.toSet());
        
        return uniqueDays.size();
    }

    // Inner Classes
    private static class UserSimilarity {
        private String userId;
        private double similarity;
        
        public UserSimilarity(String userId, double similarity) {
            this.userId = userId;
            this.similarity = similarity;
        }
        
        public String getUserId() { return userId; }
        public double getSimilarity() { return similarity; }
    }
    
    private static class ProblemScore {
        private Problem problem;
        private double score;
        
        public ProblemScore(Problem problem, double score) {
            this.problem = problem;
            this.score = score;
        }
        
        public Problem getProblem() { return problem; }
        public double getScore() { return score; }
    }
    
    private static class UserProfile {
        private Set<String> solvedProblemIds;
        private Map<String, Integer> preferredTopics;
        private Map<String, Integer> preferredDifficulties;
        
        public Set<String> getSolvedProblemIds() { return solvedProblemIds; }
        public void setSolvedProblemIds(Set<String> solvedProblemIds) { this.solvedProblemIds = solvedProblemIds; }
        
        public Map<String, Integer> getPreferredTopics() { return preferredTopics; }
        public void setPreferredTopics(Map<String, Integer> preferredTopics) { this.preferredTopics = preferredTopics; }
        
        public Map<String, Integer> getPreferredDifficulties() { return preferredDifficulties; }
        public void setPreferredDifficulties(Map<String, Integer> preferredDifficulties) { this.preferredDifficulties = preferredDifficulties; }
    }
}
