package com.playbook.shelf_service.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CompletedBook {
    private String bookId;
    private String bookTitle;
    private String bookCover;
    private Date completedAt;
    private Long timeSpentSeconds;      // общее время чтения книги
}