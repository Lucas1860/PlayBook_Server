package com.playbook.gamification_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "user_achievements")
@Data
@CompoundIndex(name = "user_achievement_idx", def = "{'userId': 1, 'achievementId': 1}", unique = true)
public class UserAchievement {
    @Id
    private String id;
    private String userId;
    private String achievementId;
    private Date earnedAt;
    private String bookId;
}