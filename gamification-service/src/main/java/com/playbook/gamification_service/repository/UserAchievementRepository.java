package com.playbook.gamification_service.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.playbook.gamification_service.entity.UserAchievement;


public interface UserAchievementRepository extends MongoRepository<UserAchievement, String> {
    List<UserAchievement> findByUserId(String userId);
    boolean existsByUserIdAndAchievementId(String userId, String achievementId);
}