package com.playbook.gamification_service.controller;

import com.playbook.gamification_service.service.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/gamification")
@RequiredArgsConstructor
public class GamificationController {

    private final GamificationService gamificationService;

    @PostMapping("/check-achievements")
    public ResponseEntity<Void> checkAchievements(@RequestBody Map<String, Object> payload) {
        String userId = (String) payload.get("userId");
        String bookId = (String) payload.get("bookId");
        Map<String, Integer> scores = (Map<String, Integer>) payload.get("achievementScores");
        gamificationService.checkAndAwardAchievements(userId, bookId, scores);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/award-figurine")
    public ResponseEntity<Void> awardFigurine(@RequestBody Map<String, Object> payload) {
        String userId = (String) payload.get("userId");
        String bookId = (String) payload.get("bookId");
        Map<String, Integer> charScores = (Map<String, Integer>) payload.get("characterScores");
        gamificationService.awardFigurine(userId, bookId, charScores);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}/achievements")
    public ResponseEntity<?> getUserAchievements(@PathVariable String userId) {
        return ResponseEntity.ok(gamificationService.getUserAchievements(userId));
    }

    @GetMapping("/users/{userId}/figurines")
    public ResponseEntity<?> getUserFigurines(@PathVariable String userId) {
        return ResponseEntity.ok(gamificationService.getUserFigurines(userId));
    }
}