package com.playbook.book_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Map;

@Document(collection = "questions")
@Data
public class Question {
    @Id
    private String id;
    private String bookId;
    private String text;
    private List<QuestionOption> options;
    private Integer order;
}