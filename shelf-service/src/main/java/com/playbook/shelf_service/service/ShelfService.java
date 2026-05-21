package com.playbook.shelf_service.service;


import com.playbook.shelf_service.dto.AddCompletedBookRequest;
import com.playbook.shelf_service.entity.CompletedBook;
import com.playbook.shelf_service.entity.Shelf;
import com.playbook.shelf_service.repository.ShelfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShelfService {

    private final ShelfRepository shelfRepository;

    // Получить или создать полку для пользователя
    public Shelf getOrCreateShelf(String userId) {
        return shelfRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Shelf newShelf = new Shelf();
                    newShelf.setUserId(userId);
                    newShelf.setName("Моя полка");
                    newShelf.setPublic(false);
                    newShelf.setBooks(new ArrayList<>());
                    newShelf.setCreatedAt(new Date());
                    newShelf.setUpdatedAt(new Date());
                    return shelfRepository.save(newShelf);
                });
    }

    // Добавить прочитанную книгу на полку
    public Shelf addCompletedBook(String userId, AddCompletedBookRequest request) {
        Shelf shelf = getOrCreateShelf(userId);
        // Проверяем, нет ли уже этой книги
        boolean exists = shelf.getBooks().stream().anyMatch(b -> b.getBookId().equals(request.getBookId()));
        if (!exists) {
            CompletedBook completed = new CompletedBook();
            completed.setBookId(request.getBookId());
            completed.setBookTitle(request.getBookTitle());
            completed.setBookCover(request.getBookCover());
            completed.setCompletedAt(new Date());
            completed.setTimeSpentSeconds(request.getTimeSpentSeconds());
            shelf.getBooks().add(completed);
            shelf.setUpdatedAt(new Date());
            shelfRepository.save(shelf);
        }
        return shelf;
    }

    // Получить полку пользователя (со всеми прочитанными книгами)
    public Shelf getUserShelf(String userId) {
        return getOrCreateShelf(userId);
    }

    // Обновить настройки полки (имя, публичность)
    public Shelf updateShelf(String userId, String name, Boolean isPublic) {
        Shelf shelf = getOrCreateShelf(userId);
        if (name != null && !name.isBlank()) shelf.setName(name);
        if (isPublic != null) shelf.setPublic(isPublic);
        shelf.setUpdatedAt(new Date());
        return shelfRepository.save(shelf);
    }

    // Получить публичные полки (для раздела "Публичные полки")
    public List<Shelf> getPublicShelves() {
        return shelfRepository.findByIsPublicTrue();
    }

    // Получить полку другого пользователя (если она публичная или запрос от админа)
    public Optional<Shelf> getShelfByUserId(String targetUserId, String requesterUserId, boolean isAdmin) {
        Optional<Shelf> shelfOpt = shelfRepository.findByUserId(targetUserId);
        if (shelfOpt.isPresent()) {
            Shelf shelf = shelfOpt.get();
            if (shelf.isPublic() || targetUserId.equals(requesterUserId) || isAdmin) {
                return shelfOpt;
            }
        }
        return Optional.empty();
    }
}