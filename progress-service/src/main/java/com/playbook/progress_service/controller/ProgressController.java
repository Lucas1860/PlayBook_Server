package com.playbook.progress_service.controller;

import com.playbook.progress_service.dto.AnswerEvent;
import com.playbook.progress_service.dto.ProgressResponse;
import com.playbook.progress_service.service.ReadingProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ReadingProgressService progressService;

    @PostMapping("/start")
    public ResponseEntity<?> startReading(@RequestParam String userId, @RequestParam String bookId) {
        return ResponseEntity.ok(progressService.startReading(userId, bookId));
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<Void> heartbeat(@RequestParam String userId, @RequestParam String bookId, @RequestParam long seconds) {
        progressService.updateTimeSpent(userId, bookId, seconds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/answer")
    public ResponseEntity<ProgressResponse> submitAnswer(@RequestBody AnswerEvent answer) {
        return ResponseEntity.ok(progressService.submitAnswer(answer));
    }

    @GetMapping("/{userId}/{bookId}")
    public ResponseEntity<ProgressResponse> getProgress(@PathVariable String userId, @PathVariable String bookId) {
        return ResponseEntity.ok(progressService.getProgress(userId, bookId));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ProgressResponse>> getBatchProgress(@RequestBody List<String> userIds, @RequestParam String bookId) {
        return ResponseEntity.ok(progressService.getProgressForUsers(userIds, bookId));
    }
}