package com.playbook.book_service.service;

import com.playbook.book_service.entity.Book;
import com.playbook.book_service.entity.QuestionAnchor;
import com.playbook.book_service.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionAnchorService {
    private final BookRepository bookRepository;

    public void addAnchor(String bookId, String questionId, Integer startOffset, Integer endOffset, Integer order) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        QuestionAnchor anchor = new QuestionAnchor();
        anchor.setQuestionId(questionId);
        anchor.setStartOffset(startOffset);
        anchor.setEndOffset(endOffset);
        anchor.setOrder(order);
        book.getQuestionAnchors().add(anchor);
        bookRepository.save(book);
    }

    public void removeAnchor(String bookId, String questionId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        book.getQuestionAnchors().removeIf(a -> a.getQuestionId().equals(questionId));
        bookRepository.save(book);
    }
}