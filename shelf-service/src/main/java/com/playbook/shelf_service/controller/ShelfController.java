package com.playbook.shelf_service.controller;

import com.playbook.shelf_service.dto.AddCompletedBookRequest;
import com.playbook.shelf_service.entity.Shelf;
import com.playbook.shelf_service.service.ShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shelves")
@RequiredArgsConstructor
public class ShelfController {

    private final ShelfService shelfService;

    @GetMapping("/me")
    public ResponseEntity<Shelf> getMyShelf(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(shelfService.getUserShelf(userId));
    }

    @PostMapping("/me/books")
    public ResponseEntity<Shelf> addCompletedBook(@RequestHeader("X-User-Id") String userId,
                                                  @RequestBody AddCompletedBookRequest request) {
        return ResponseEntity.ok(shelfService.addCompletedBook(userId, request));
    }

    @PutMapping("/me")
    public ResponseEntity<Shelf> updateShelf(@RequestHeader("X-User-Id") String userId,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) Boolean isPublic) {
        return ResponseEntity.ok(shelfService.updateShelf(userId, name, isPublic));
    }

    @GetMapping("/public")
    public ResponseEntity<?> getPublicShelves() {
        return ResponseEntity.ok(shelfService.getPublicShelves());
    }

    @GetMapping("/user/{targetUserId}")
    public ResponseEntity<Shelf> getUserShelf(@RequestHeader("X-User-Id") String requesterUserId,
                                              @PathVariable String targetUserId,
                                              @RequestParam(defaultValue = "false") boolean isAdmin) {
        return shelfService.getShelfByUserId(targetUserId, requesterUserId, isAdmin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}