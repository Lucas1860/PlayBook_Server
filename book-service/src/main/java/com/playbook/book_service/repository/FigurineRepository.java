package com.playbook.book_service.repository;

import com.playbook.book_service.entity.Figurine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FigurineRepository extends MongoRepository<Figurine, String> {
    List<Figurine> findByBookId(String bookId);
    void deleteByBookId(String bookId);
}
