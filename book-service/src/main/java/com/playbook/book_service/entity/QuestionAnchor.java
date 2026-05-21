package com.playbook.book_service.entity;

import lombok.Data;

@Data
public class QuestionAnchor {
    private String questionId;   // ссылка на ObjectId вопроса
    private Integer startOffset;
    private Integer endOffset;
    private Integer order;       // порядковый номер вопроса
}