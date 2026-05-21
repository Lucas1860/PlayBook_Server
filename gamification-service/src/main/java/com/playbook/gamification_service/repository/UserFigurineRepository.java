package com.playbook.gamification_service.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.playbook.gamification_service.entity.UserFigurine;


public interface UserFigurineRepository extends MongoRepository<UserFigurine, String> {
    List<UserFigurine> findByUserId(String userId);
    boolean existsByUserIdAndFigurineId(String userId, String figurineId);
}