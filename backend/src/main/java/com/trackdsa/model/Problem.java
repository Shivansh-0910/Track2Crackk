package com.trackdsa.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Date;

@Document(collection = "problems")
public class Problem {
    @Id
    private String id;
    private String name;
    private String description;
    private String topic;
    private String difficulty; // Easy, Medium, Hard
    private List<String> companies;
    private String url;
    private int frequencyScore; // 1-10 based on interview frequency
    private List<String> tags;
    private List<String> patterns; // Algorithm patterns
    private int timeComplexity;
    private int spaceComplexity;
    private double averageTime; // Average solving time in minutes
    private Date createdAt = new Date();
    private boolean isActive = true;

    // Constructors
    public Problem() {}

    public Problem(String name, String topic, String difficulty) {
        this.name = name;
        this.topic = topic;
        this.difficulty = difficulty;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public List<String> getCompanies() { return companies; }
    public void setCompanies(List<String> companies) { this.companies = companies; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public int getFrequencyScore() { return frequencyScore; }
    public void setFrequencyScore(int frequencyScore) { this.frequencyScore = frequencyScore; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public List<String> getPatterns() { return patterns; }
    public void setPatterns(List<String> patterns) { this.patterns = patterns; }

    public int getTimeComplexity() { return timeComplexity; }
    public void setTimeComplexity(int timeComplexity) { this.timeComplexity = timeComplexity; }

    public int getSpaceComplexity() { return spaceComplexity; }
    public void setSpaceComplexity(int spaceComplexity) { this.spaceComplexity = spaceComplexity; }

    public double getAverageTime() { return averageTime; }
    public void setAverageTime(double averageTime) { this.averageTime = averageTime; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
