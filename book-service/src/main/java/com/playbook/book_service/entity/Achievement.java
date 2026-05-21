package com.playbook.book_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;

@Document(collection = "achievements")
@Data
public class Achievement {
    @Id
    private String id;
    private String bookId;
    private String name;
    private String description;
    private String imageUrl;
    private Map<String, Integer> requiredTags;  // {"honor": 2}
}