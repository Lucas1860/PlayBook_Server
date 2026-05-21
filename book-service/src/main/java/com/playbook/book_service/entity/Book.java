package com.playbook.book_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "books")
@Data
public class Book {
    @Id
    private String id;
    private String title;
    private String author;
    private String description;
    private String coverImageUrl;
    private List<Integer> classes;      // например [5,6,7]
    private String type;                // "Русская классика" или "Зарубежная классика"
    private List<String> genres;        // ["роман", "повесть", ...]
    private String fullText;            // полный текст книги
    private List<QuestionAnchor> questionAnchors; // привязка вопросов к тексту
    private boolean active = true;      // для мягкого удаления
}
