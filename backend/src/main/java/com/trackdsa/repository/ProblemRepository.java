package com.trackdsa.repository;

import com.trackdsa.model.Problem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProblemRepository extends MongoRepository<Problem, String> {
    
    // Find problems by topic
    List<Problem> findByTopicAndIsActiveTrue(String topic);
    
    // Find problems by difficulty
    List<Problem> findByDifficultyAndIsActiveTrue(String difficulty);
    
    // Find problems by company
    List<Problem> findByCompaniesContainingAndIsActiveTrue(String company);
    
    // Find problems by multiple topics
    List<Problem> findByTopicInAndIsActiveTrue(List<String> topics);
    
    // Find problems by frequency score (high frequency first)
    List<Problem> findByIsActiveTrueOrderByFrequencyScoreDesc();
    
    // Find problems by topic and difficulty
    List<Problem> findByTopicAndDifficultyAndIsActiveTrue(String topic, String difficulty);
    
    // Custom query to find problems by multiple criteria
    @Query("{ 'topic': { $in: ?0 }, 'difficulty': ?1, 'companies': { $in: ?2 }, 'isActive': true }")
    List<Problem> findByTopicsAndDifficultyAndCompanies(List<String> topics, String difficulty, List<String> companies);
    
    // Find problems with high frequency for specific companies
    @Query("{ 'companies': { $in: ?0 }, 'frequencyScore': { $gte: ?1 }, 'isActive': true }")
    List<Problem> findHighFrequencyProblemsByCompanies(List<String> companies, int minFrequency);
    
    // Find problems by patterns (for pattern-based learning)
    List<Problem> findByPatternsContainingAndIsActiveTrue(String pattern);
    
    // Count problems by topic
    long countByTopicAndIsActiveTrue(String topic);
    
    // Count problems by difficulty
    long countByDifficultyAndIsActiveTrue(String difficulty);
}
