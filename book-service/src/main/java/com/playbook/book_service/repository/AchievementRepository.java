package com.playbook.book_service.repository;

import com.playbook.book_service.entity.Achievement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AchievementRepository extends MongoRepository<Achievement, String> {
    List<Achievement> findByBookId(String bookId);
    void deleteByBookId(String bookId);
}
