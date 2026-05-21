package com.playbook.book_service.repository;

import com.playbook.book_service.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findByBookId(String bookId);
    void deleteByBookId(String bookId);
}
