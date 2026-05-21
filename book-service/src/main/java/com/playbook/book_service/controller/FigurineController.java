package com.playbook.book_service.controller;

import com.playbook.book_service.entity.Figurine;
import com.playbook.book_service.service.FigurineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books/{bookId}/figurines")
@RequiredArgsConstructor
public class FigurineController {
    private final FigurineService figurineService;

    @GetMapping
    public ResponseEntity<List<Figurine>> getFigurines(@PathVariable String bookId) {
        return ResponseEntity.ok(figurineService.getFigurinesByBook(bookId));
    }

    @PostMapping
    public ResponseEntity<Figurine> createFigurine(@PathVariable String bookId,
                                                   @RequestBody Figurine figurine) {
        figurine.setBookId(bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body(figurineService.createFigurine(figurine));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFigurine(@PathVariable String id) {
        figurineService.deleteFigurine(id);
        return ResponseEntity.noContent().build();
    }
}