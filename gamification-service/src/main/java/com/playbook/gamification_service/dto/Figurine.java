package com.playbook.gamification_service.dto;

import lombok.Data;

@Data
public class Figurine {
    private String id;
    private String bookId;
    private String characterId;
    private String name;
    private String description;
    private String imageUrl;
}