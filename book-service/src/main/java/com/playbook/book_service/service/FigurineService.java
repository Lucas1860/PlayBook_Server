package com.playbook.book_service.service;

import com.playbook.book_service.entity.Figurine;
import com.playbook.book_service.repository.FigurineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FigurineService {
    private final FigurineRepository figurineRepository;

    public List<Figurine> getFigurinesByBook(String bookId) {
        return figurineRepository.findByBookId(bookId);
    }

    public Figurine createFigurine(Figurine figurine) {
        return figurineRepository.save(figurine);
    }

    public void deleteFigurine(String id) {
        figurineRepository.deleteById(id);
    }

    public void deleteFigurinesByBook(String bookId) {
        figurineRepository.deleteByBookId(bookId);
    }
}