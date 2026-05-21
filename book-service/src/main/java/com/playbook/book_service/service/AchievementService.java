package com.playbook.book_service.service;

import com.playbook.book_service.entity.Achievement;
import com.playbook.book_service.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;

    public List<Achievement> getAchievementsByBook(String bookId) {
        return achievementRepository.findByBookId(bookId);
    }

    public Achievement createAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    public void deleteAchievement(String id) {
        achievementRepository.deleteById(id);
    }

    public void deleteAchievementsByBook(String bookId) {
        achievementRepository.deleteByBookId(bookId);
    }
}