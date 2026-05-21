package com.playbook.book_service.entity;

import lombok.Data;
import java.util.Map;

@Data
public class QuestionOption {
    private String letter;          // "А", "Б", ...
    private String text;
    private Map<String, Integer> characterWeights;  // {"grinev": 1, "shvabrin": 0}
    private Map<String, Integer> achievementTags;   // {"honor": 2, "wisdom": 1}
}