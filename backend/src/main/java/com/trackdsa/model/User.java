package com.trackdsa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String name = "";
    private String avatar;
    private String level = "Beginner";
    private List<String> targetCompanies = new ArrayList<>();
    private String timeAvailability = "1 hour";
    private int weeklyGoal = 10;
    private boolean completedOnboarding = false;
    @JsonProperty("leetcode_username")
    private String leetcodeUsername = "";
    @JsonProperty("total_problems_solved")
    private int totalProblemsSolved = 0;
    @JsonProperty("last_submission_date")
    private Date lastSubmissionDate;
    private Date createdAt = new Date();
    private Date lastLoginAt = new Date();
    private boolean isActive = true;
    private String refreshToken = UUID.randomUUID().toString();
    private String bio = "";

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<String> getTargetCompanies() {
        return targetCompanies;
    }

    public void setTargetCompanies(List<String> targetCompanies) {
        this.targetCompanies = targetCompanies;
    }

    public String getTimeAvailability() {
        return timeAvailability;
    }

    public void setTimeAvailability(String timeAvailability) {
        this.timeAvailability = timeAvailability;
    }

    public int getWeeklyGoal() {
        return weeklyGoal;
    }

    public void setWeeklyGoal(int weeklyGoal) {
        this.weeklyGoal = weeklyGoal;
    }

    public boolean isCompletedOnboarding() {
        return completedOnboarding;
    }

    public void setCompletedOnboarding(boolean completedOnboarding) {
        this.completedOnboarding = completedOnboarding;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLeetcodeUsername() {
        return leetcodeUsername;
    }

    public void setLeetcodeUsername(String leetcodeUsername) {
        this.leetcodeUsername = leetcodeUsername;
    }

    public int getTotalProblemsSolved() {
        return totalProblemsSolved;
    }

    public void setTotalProblemsSolved(int totalProblemsSolved) {
        this.totalProblemsSolved = totalProblemsSolved;
    }

    public Date getLastSubmissionDate() {
        return lastSubmissionDate;
    }

    public void setLastSubmissionDate(Date lastSubmissionDate) {
        this.lastSubmissionDate = lastSubmissionDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
} 
