package com.playbook.progress_service.repository;

import com.playbook.progress_service.entity.UserReadingProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface UserProgressRepository extends MongoRepository<UserReadingProgress, String> {
    Optional<UserReadingProgress> findByUserIdAndBookId(String userId, String bookId);
    List<UserReadingProgress> findByUserId(String userId);
    List<UserReadingProgress> findByBookIdAndCompletedTrue(String bookId);
}