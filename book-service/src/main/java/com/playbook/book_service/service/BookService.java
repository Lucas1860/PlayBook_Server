package com.playbook.book_service.service;

import com.playbook.book_service.entity.Book;
import com.playbook.book_service.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAllByActiveTrue();
    }

    public Book getBookById(String id) {
        return bookRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
    }

    public Book createBook(Book book) {
        book.setActive(true);
        if (book.getQuestionAnchors() == null) book.setQuestionAnchors(List.of());
        return bookRepository.save(book);
    }

    public void deleteBook(String id) {
        Book book = getBookById(id);
        book.setActive(false);
        bookRepository.save(book);
    }
}