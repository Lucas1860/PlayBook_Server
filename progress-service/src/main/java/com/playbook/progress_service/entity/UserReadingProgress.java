package com.playbook.progress_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Document(collection = "user_progress")
@Data
@CompoundIndex(name = "user_book_idx", def = "{'userId': 1, 'bookId': 1}", unique = true)
public class UserReadingProgress {
    @Id
    private String id;
    private String userId;
    private String bookId;
    private Set<String> answeredQuestionIds = new HashSet<>();
    private int totalQuestions;           // общее количество вопросов в книге
    private long timeSpentSeconds = 0;    // общее время чтения (сек)
    private boolean completed = false;
    private Date completedAt;
    private Date lastActivityAt;

    private Map<String, Integer> characterScores;   // {"grinev": 5, "masha": 2}
    private Map<String, Integer> achievementScores; // {"honor": 3, "wisdom": 1}
}