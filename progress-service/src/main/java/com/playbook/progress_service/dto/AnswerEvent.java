package com.playbook.progress_service.dto;

import lombok.Data;
import java.util.Map;

@Data
public class AnswerEvent {
    private String userId;
    private String bookId;
    private String questionId;
    private String selectedOptionLetter;
    private Map<String, Integer> characterWeights;   // веса за этот ответ
    private Map<String, Integer> achievementTags;
}