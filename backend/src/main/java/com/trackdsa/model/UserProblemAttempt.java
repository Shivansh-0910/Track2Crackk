package com.trackdsa.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "user_problem_attempts")
public class UserProblemAttempt {
    @Id
    private String id;
    private String userId;
    private String problemId;
    private String status; // ATTEMPTED, SOLVED, REVIEWED, SKIPPED
    private int timeSpent; // in minutes
    private int attempts;
    private String difficulty; // User's perceived difficulty
    private String approach; // User's solution approach
    private double confidenceLevel; // 1-5 scale
    private boolean needsReview;
    private Date attemptDate = new Date();
    private Date lastReviewDate;

    // Constructors
    public UserProblemAttempt() {}

    public UserProblemAttempt(String userId, String problemId, String status) {
        this.userId = userId;
        this.problemId = problemId;
        this.status = status;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getProblemId() { return problemId; }
    public void setProblemId(String problemId) { this.problemId = problemId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTimeSpent() { return timeSpent; }
    public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }

    public int getAttempts() { return attempts; }
    public void setAttempts(int attempts) { this.attempts = attempts; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getApproach() { return approach; }
    public void setApproach(String approach) { this.approach = approach; }

    public double getConfidenceLevel() { return confidenceLevel; }
    public void setConfidenceLevel(double confidenceLevel) { this.confidenceLevel = confidenceLevel; }

    public boolean isNeedsReview() { return needsReview; }
    public void setNeedsReview(boolean needsReview) { this.needsReview = needsReview; }

    public Date getAttemptDate() { return attemptDate; }
    public void setAttemptDate(Date attemptDate) { this.attemptDate = attemptDate; }

    public Date getLastReviewDate() { return lastReviewDate; }
    public void setLastReviewDate(Date lastReviewDate) { this.lastReviewDate = lastReviewDate; }
}
