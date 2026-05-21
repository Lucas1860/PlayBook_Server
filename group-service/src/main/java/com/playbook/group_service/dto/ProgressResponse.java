package com.playbook.group_service.dto;

import lombok.Data;
import java.util.Date;


@Data
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