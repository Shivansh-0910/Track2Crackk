package com.trackdsa.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.Map;

@Document(collection = "recommendation_feedback")
public class RecommendationFeedback {
    @Id
    private String id;
    private String userId;
    private String problemId;
    private String recommendationType; // daily, weekly, skill-gap, topic-specific, company-specific
    private String feedbackType; // positive, negative, neutral
    private double recommendationScore; // Original recommendation score
    private Map<String, Double> featureScores; // Individual feature scores that led to recommendation
    private String userComment; // Optional user comment
    private boolean wasAttempted; // Did user actually attempt the problem
    private boolean wasSolved; // Did user solve the problem
    private int timeSpent; // Time spent on problem (if attempted)
    private Date feedbackDate = new Date();
    private Date createdAt = new Date();

    // Constructors
    public RecommendationFeedback() {}

    public RecommendationFeedback(String userId, String problemId, String feedbackType) {
        this.userId = userId;
        this.problemId = problemId;
        this.feedbackType = feedbackType;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getProblemId() { return problemId; }
    public void setProblemId(String problemId) { this.problemId = problemId; }

    public String getRecommendationType() { return recommendationType; }
    public void setRecommendationType(String recommendationType) { this.recommendationType = recommendationType; }

    public String getFeedbackType() { return feedbackType; }
    public void setFeedbackType(String feedbackType) { this.feedbackType = feedbackType; }

    public double getRecommendationScore() { return recommendationScore; }
    public void setRecommendationScore(double recommendationScore) { this.recommendationScore = recommendationScore; }

    public Map<String, Double> getFeatureScores() { return featureScores; }
    public void setFeatureScores(Map<String, Double> featureScores) { this.featureScores = featureScores; }

    public String getUserComment() { return userComment; }
    public void setUserComment(String userComment) { this.userComment = userComment; }

    public boolean isWasAttempted() { return wasAttempted; }
    public void setWasAttempted(boolean wasAttempted) { this.wasAttempted = wasAttempted; }

    public boolean isWasSolved() { return wasSolved; }
    public void setWasSolved(boolean wasSolved) { this.wasSolved = wasSolved; }

    public int getTimeSpent() { return timeSpent; }
    public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }

    public Date getFeedbackDate() { return feedbackDate; }
    public void setFeedbackDate(Date feedbackDate) { this.feedbackDate = feedbackDate; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
