package com.playbook.shelf_service.dto;

import lombok.Data;

@Data
public class AddCompletedBookRequest {
    private String bookId;
    private String bookTitle;
    private String bookCover;
    private Long timeSpentSeconds;
}