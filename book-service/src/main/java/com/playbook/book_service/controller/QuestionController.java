package com.playbook.book_service.controller;

import com.playbook.book_service.entity.Question;
import com.playbook.book_service.service.QuestionAnchorService;
import com.playbook.book_service.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books/{bookId}/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionAnchorService anchorService;

    @GetMapping
    public ResponseEntity<List<Question>> getQuestions(@PathVariable String bookId) {
        return ResponseEntity.ok(questionService.getQuestionsByBook(bookId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable String id) {
        return ResponseEntity.ok(questionService.getQuestion(id));
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(@PathVariable String bookId,
                                                   @RequestBody Question question) {
        question.setBookId(bookId);
        Question saved = questionService.createQuestion(question);
        // anchor будет добавлен отдельным вызовом, но можно и здесь
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String bookId,
                                               @PathVariable String id) {
        anchorService.removeAnchor(bookId, id);
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}