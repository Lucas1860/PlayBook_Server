package com.playbook.book_service.service;

import com.playbook.book_service.entity.Question;
import com.playbook.book_service.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> getQuestionsByBook(String bookId) {
        return questionRepository.findByBookId(bookId);
    }

    public Question getQuestion(String id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Вопрос не найден"));
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void deleteQuestion(String id) {
        questionRepository.deleteById(id);
    }

    public void deleteQuestionsByBook(String bookId) {
        questionRepository.deleteByBookId(bookId);
    }
}