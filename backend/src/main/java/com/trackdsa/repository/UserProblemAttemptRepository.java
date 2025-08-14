package com.trackdsa.repository;

import com.trackdsa.model.UserProblemAttempt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserProblemAttemptRepository extends MongoRepository<UserProblemAttempt, String> {
    
    // Find user's attempt for a specific problem
    Optional<UserProblemAttempt> findByUserIdAndProblemId(String userId, String problemId);
    
    // Find all attempts by user
    List<UserProblemAttempt> findByUserId(String userId);
    
    // Find user's attempts by status
    List<UserProblemAttempt> findByUserIdAndStatus(String userId, String status);
    
    // Find user's recent attempts
    List<UserProblemAttempt> findByUserIdOrderByAttemptDateDesc(String userId);
    
    // Find attempts that need review
    List<UserProblemAttempt> findByUserIdAndNeedsReviewTrue(String userId);
    
    // Find attempts by date range
    List<UserProblemAttempt> findByUserIdAndAttemptDateBetween(String userId, Date startDate, Date endDate);
    
    // Count solved problems by user
    long countByUserIdAndStatus(String userId, String status);
    
    // Custom query to get user's topic-wise statistics
    @Query("{ 'userId': ?0, 'status': 'SOLVED' }")
    List<UserProblemAttempt> findSolvedProblemsByUser(String userId);
    
    // Find low confidence problems for review
    @Query("{ 'userId': ?0, 'confidenceLevel': { $lt: ?1 }, 'status': 'SOLVED' }")
    List<UserProblemAttempt> findLowConfidenceProblems(String userId, double maxConfidence);
    
    // Find problems attempted multiple times
    @Query("{ 'userId': ?0, 'attempts': { $gt: 1 } }")
    List<UserProblemAttempt> findMultipleAttemptProblems(String userId);
}
