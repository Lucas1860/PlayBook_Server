package com.playbook.gamification_service.dto;

import lombok.Data;
import java.util.Map;

@Data
public class Achievement {
    private String id;
    private String bookId;
    private String name;
    private String description;
    private String imageUrl;
    private Map<String, Integer> requiredTags;
}