package com.playbook.shelf_service.repository;

import com.playbook.shelf_service.entity.Shelf;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ShelfRepository extends MongoRepository<Shelf, String> {
    Optional<Shelf> findByUserId(String userId);
    List<Shelf> findByIsPublicTrue();
}