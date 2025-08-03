package com.trackdsa.repository;

import com.trackdsa.model.UserProblem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserProblemRepository extends MongoRepository<UserProblem, String> {
    Optional<UserProblem> findByUserIdAndProblemId(String userId, String problemId);
}