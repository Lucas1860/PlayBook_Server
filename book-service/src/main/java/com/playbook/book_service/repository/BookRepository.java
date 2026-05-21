package com.playbook.book_service.repository;

import com.playbook.book_service.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findAllByActiveTrue();
    Optional<Book> findByIdAndActiveTrue(String id);
}

