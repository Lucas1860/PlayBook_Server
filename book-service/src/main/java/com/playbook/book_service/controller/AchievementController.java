package com.playbook.book_service.controller;

import com.playbook.book_service.entity.Achievement;
import com.playbook.book_service.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books/{bookId}/achievements")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping
    public ResponseEntity<List<Achievement>> getAchievements(@PathVariable String bookId) {
        return ResponseEntity.ok(achievementService.getAchievementsByBook(bookId));
    }

    @PostMapping
    public ResponseEntity<Achievement> createAchievement(@PathVariable String bookId,
                                                         @RequestBody Achievement achievement) {
        achievement.setBookId(bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body(achievementService.createAchievement(achievement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable String id) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }
}