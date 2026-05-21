package com.playbook.progress_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
public class ProgressResponse {
    private String userId;
    private String bookId;
    private int answeredCount;
    private int totalQuestions;
    private int percentComplete;
    private long timeSpentSeconds;
    private boolean completed;
    private Date completedAt;
}