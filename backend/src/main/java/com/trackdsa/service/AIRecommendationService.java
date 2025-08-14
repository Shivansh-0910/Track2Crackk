package com.trackdsa.service;

import com.trackdsa.model.Problem;
import com.trackdsa.model.User;
import com.trackdsa.model.UserProblemAttempt;
import com.trackdsa.repository.ProblemRepository;
import com.trackdsa.repository.UserProblemAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
public class AIRecommendationService {

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private UserProblemAttemptRepository attemptRepository;

    @Autowired
    private UserService userService;

    // Main recommendation method
    public List<RecommendationResult> getPersonalizedRecommendations(String userId, int limit) {
        User user = userService.findById(userId).orElseThrow(() -> 
            new RuntimeException("User not found"));

        List<Problem> allProblems = problemRepository.findByIsActiveTrueOrderByFrequencyScoreDesc();
        
        // If no problems in database, return empty list
        if (allProblems.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<UserProblemAttempt> userAttempts = attemptRepository.findByUserId(userId);
        
        // Filter out already solved problems
        Set<String> solvedProblemIds = userAttempts.stream()
            .filter(attempt -> "SOLVED".equals(attempt.getStatus()))
            .map(UserProblemAttempt::getProblemId)
            .collect(Collectors.toSet());
        
        List<Problem> candidateProblems = allProblems.stream()
            .filter(problem -> !solvedProblemIds.contains(problem.getId()))
            .collect(Collectors.toList());

        // Calculate recommendation scores
        List<RecommendationResult> recommendations = candidateProblems.stream()
            .map(problem -> calculateRecommendationScore(user, problem, userAttempts))
            .sorted((r1, r2) -> Double.compare(r2.getScore(), r1.getScore()))
            .limit(limit)
            .collect(Collectors.toList());

        return recommendations;
    }

    // Helper method to get total problems count
    public long getTotalProblemsCount() {
        return problemRepository.count();
    }

    // Calculate comprehensive recommendation score
    private RecommendationResult calculateRecommendationScore(User user, Problem problem, List<UserProblemAttempt> userAttempts) {
        double skillGapScore = calculateSkillGapScore(user, problem, userAttempts);
        double difficultyScore = calculateDifficultyProgressionScore(user, problem, userAttempts);
        double companyScore = calculateCompanyRelevanceScore(user, problem);
        double timeDecayScore = calculateTimeDecayScore(user, problem, userAttempts);
        double patternScore = calculatePatternLearningScore(user, problem, userAttempts);
        double preferenceScore = calculateUserPreferenceScore(user, problem);

        // Weighted combination
        double totalScore = skillGapScore * 0.25 +
                           difficultyScore * 0.20 +
                           companyScore * 0.20 +
                           timeDecayScore * 0.15 +
                           patternScore * 0.10 +
                           preferenceScore * 0.10;

        return new RecommendationResult(problem, totalScore, generateReasoningText(
            skillGapScore, difficultyScore, companyScore, timeDecayScore, patternScore, preferenceScore
        ));
    }

    // 1. Skill Gap Analysis
    private double calculateSkillGapScore(User user, Problem problem, List<UserProblemAttempt> userAttempts) {
        String topic = problem.getTopic();
        
        // Get user's performance in this topic
        List<UserProblemAttempt> topicAttempts = userAttempts.stream()
            .filter(attempt -> {
                // Filter by topic-related attempts
                // In a real implementation, you'd join with Problem table to get the actual topic
                String attemptTopic = getTopicForAttempt(attempt); // This helper method gets topic for attempt
                return topic.equals(attemptTopic) && 
                       (attempt.getStatus().equals("SOLVED") || attempt.getStatus().equals("ATTEMPTED"));
            })
            .collect(Collectors.toList());

        if (topicAttempts.isEmpty()) {
            return 1.0; // High score for unexplored topics
        }

        // Calculate accuracy in topic
        long solvedCount = topicAttempts.stream()
            .filter(attempt -> "SOLVED".equals(attempt.getStatus()))
            .count();
        
        double accuracy = (double) solvedCount / topicAttempts.size();
        
        // Calculate recency factor
        double recencyFactor = calculateRecencyFactor(topicAttempts);
        
        // Higher score for low accuracy or stale topics
        return (1.0 - accuracy) * recencyFactor;
    }

    // 2. Difficulty Progression Score
    private double calculateDifficultyProgressionScore(User user, Problem problem, List<UserProblemAttempt> userAttempts) {
        double userLevel = assessUserLevel(user, userAttempts);
        double problemDifficulty = getDifficultyValue(problem.getDifficulty());
        
        // Optimal difficulty is slightly above current level
        double optimalDifficulty = userLevel + 0.2;
        double difficultyGap = Math.abs(problemDifficulty - optimalDifficulty);
        
        // Score decreases as gap increases from optimal
        return Math.max(0.0, 1.0 - difficultyGap);
    }

    // 3. Company Relevance Score
    private double calculateCompanyRelevanceScore(User user, Problem problem) {
        List<String> targetCompanies = user.getTargetCompanies();
        List<String> problemCompanies = problem.getCompanies();
        
        if (targetCompanies == null || targetCompanies.isEmpty()) {
            return 0.5; // Neutral score if no target companies
        }
        
        double relevanceScore = 0.0;
        for (String company : targetCompanies) {
            if (problemCompanies != null && problemCompanies.contains(company)) {
                relevanceScore += getCompanyWeight(company);
            }
        }
        
        // Boost for high-frequency problems
        double frequencyBoost = problem.getFrequencyScore() / 10.0;
        relevanceScore *= (1.0 + frequencyBoost);
        
        return Math.min(1.0, relevanceScore);
    }

    // 4. Time Decay Score (encourages revisiting old topics)
    private double calculateTimeDecayScore(User user, Problem problem, List<UserProblemAttempt> userAttempts) {
        String topic = problem.getTopic();
        
        // Find most recent attempt in this topic
        Optional<Date> lastAttempt = userAttempts.stream()
            .filter(attempt -> topic.equals(getTopicForAttempt(attempt))) // Simplified
            .map(UserProblemAttempt::getAttemptDate)
            .max(Date::compareTo);
        
        if (!lastAttempt.isPresent()) {
            return 1.0; // High score for never attempted topics
        }
        
        // Calculate days since last attempt
        LocalDateTime lastAttemptTime = lastAttempt.get().toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDateTime();
        long daysSince = ChronoUnit.DAYS.between(lastAttemptTime, LocalDateTime.now());
        
        // Score increases with time (forgetting curve)
        return Math.min(1.0, daysSince / 30.0); // Normalize to 30 days
    }

    // 5. Pattern Learning Score
    private double calculatePatternLearningScore(User user, Problem problem, List<UserProblemAttempt> userAttempts) {
        List<String> problemPatterns = problem.getPatterns();
        if (problemPatterns == null || problemPatterns.isEmpty()) {
            return 0.5; // Neutral score
        }
        
        // Check which patterns user has experience with
        // This would require more complex analysis of solved problems
        // Simplified for now
        return 0.7; // Default pattern score
    }

    // 6. User Preference Score
    private double calculateUserPreferenceScore(User user, Problem problem) {
        // Based on user's confident/struggling topics from onboarding
        // This would be implemented based on your User model structure
        return 0.6; // Default preference score
    }

    // Helper methods
    private double calculateRecencyFactor(List<UserProblemAttempt> attempts) {
        if (attempts.isEmpty()) return 1.0;
        
        Date mostRecentAttempt = attempts.stream()
            .map(UserProblemAttempt::getAttemptDate)
            .max(Date::compareTo)
            .orElse(new Date(0));
        
        LocalDateTime recentTime = mostRecentAttempt.toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDateTime();
        long daysSince = ChronoUnit.DAYS.between(recentTime, LocalDateTime.now());
        
        return Math.min(1.0, daysSince / 14.0); // Normalize to 2 weeks
    }

    private double assessUserLevel(User user, List<UserProblemAttempt> userAttempts) {
        // Simple level assessment based on solved problems
        long solvedCount = userAttempts.stream()
            .filter(attempt -> "SOLVED".equals(attempt.getStatus()))
            .count();
        
        if (solvedCount < 10) return 0.3; // Beginner
        if (solvedCount < 50) return 0.6; // Intermediate
        return 0.9; // Advanced
    }

    private double getDifficultyValue(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy": return 0.3;
            case "medium": return 0.6;
            case "hard": return 0.9;
            default: return 0.5;
        }
    }

    private double getCompanyWeight(String company) {
        // Higher weights for FAANG companies
        Map<String, Double> weights = Map.of(
            "Google", 1.0,
            "Amazon", 1.0,
            "Microsoft", 1.0,
            "Meta", 1.0,
            "Apple", 1.0,
            "Netflix", 0.8,
            "Uber", 0.8
        );
        return weights.getOrDefault(company, 0.6);
    }

    private String getTopicForAttempt(UserProblemAttempt attempt) {
        // This would require joining with Problem table
        // Simplified for now
        return "Arrays"; // Default topic
    }

    private String generateReasoningText(double skillGap, double difficulty, double company, 
                                       double timeDecay, double pattern, double preference) {
        List<String> reasons = new ArrayList<>();
        
        if (skillGap > 0.7) reasons.add("Addresses a skill gap");
        if (difficulty > 0.7) reasons.add("Optimal difficulty level");
        if (company > 0.7) reasons.add("Relevant to target companies");
        if (timeDecay > 0.7) reasons.add("Good time to revisit this topic");
        if (pattern > 0.7) reasons.add("Builds on learned patterns");
        
        return reasons.isEmpty() ? "Recommended for overall growth" : String.join(", ", reasons);
    }

    // Result class
    public static class RecommendationResult {
        private Problem problem;
        private double score;
        private String reasoning;

        public RecommendationResult(Problem problem, double score, String reasoning) {
            this.problem = problem;
            this.score = score;
            this.reasoning = reasoning;
        }

        // Getters
        public Problem getProblem() { return problem; }
        public double getScore() { return score; }
        public String getReasoning() { return reasoning; }
    }
}
