package com.playbook.gamification_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "user_figurines")
@Data
@CompoundIndex(name = "user_figurine_idx", def = "{'userId': 1, 'figurineId': 1}", unique = true)
public class UserFigurine {
    @Id
    private String id;
    private String userId;
    private String figurineId;
    private Date earnedAt;
    private String bookId;
}