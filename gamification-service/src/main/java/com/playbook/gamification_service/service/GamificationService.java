package com.playbook.gamification_service.service;

import com.playbook.gamification_service.dto.Achievement;
import com.playbook.gamification_service.dto.Figurine;
import com.playbook.gamification_service.entity.UserAchievement;
import com.playbook.gamification_service.entity.UserFigurine;
import com.playbook.gamification_service.repository.UserAchievementRepository;
import com.playbook.gamification_service.repository.UserFigurineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GamificationService {

    private final UserAchievementRepository userAchievementRepository;
    private final UserFigurineRepository userFigurineRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${book.service.url}")
    private String bookServiceUrl;

    // Проверка и выдача достижений на основе текущих achievementScores
    public void checkAndAwardAchievements(String userId, String bookId, Map<String, Integer> currentScores) {
        // Получаем все достижения для данной книги
        Achievement[] achievements = restTemplate.getForObject(bookServiceUrl + "/books/" + bookId + "/achievements", Achievement[].class);
        if (achievements == null) return;

        for (Achievement ach : achievements) {
            // Если уже есть достижение – пропускаем
            if (userAchievementRepository.existsByUserIdAndAchievementId(userId, ach.getId())) {
                continue;
            }
            // Проверяем условия: requiredTags
            boolean meets = true;
            for (Map.Entry<String, Integer> required : ach.getRequiredTags().entrySet()) {
                int current = currentScores.getOrDefault(required.getKey(), 0);
                if (current < required.getValue()) {
                    meets = false;
                    break;
                }
            }
            if (meets) {
                UserAchievement userAchievement = new UserAchievement();
                userAchievement.setUserId(userId);
                userAchievement.setAchievementId(ach.getId());
                userAchievement.setBookId(bookId);
                userAchievement.setEarnedAt(new Date());
                userAchievementRepository.save(userAchievement);
                // Здесь можно отправить событие через WebSocket или просто сохранить
            }
        }
    }

    // Выдача фигурки по завершении книги
    public void awardFigurine(String userId, String bookId, Map<String, Integer> characterScores) {
        // Получить все фигурки для данной книги
        Figurine[] figurines = restTemplate.getForObject(bookServiceUrl + "/books/" + bookId + "/figurines", Figurine[].class);
        if (figurines == null || figurines.length == 0) return;

        // Определить персонажа с максимальным счетом
        String topCharacter = characterScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("neutral");

        // Найти фигурку, соответствующую topCharacter (или "neutral")
        Figurine awarded = Arrays.stream(figurines)
                .filter(f -> f.getCharacterId().equals(topCharacter))
                .findFirst()
                .orElse(null);
        if (awarded == null) {
            awarded = Arrays.stream(figurines)
                    .filter(f -> "neutral".equals(f.getCharacterId()))
                    .findFirst()
                    .orElse(null);
        }
        if (awarded != null && !userFigurineRepository.existsByUserIdAndFigurineId(userId, awarded.getId())) {
            UserFigurine userFigurine = new UserFigurine();
            userFigurine.setUserId(userId);
            userFigurine.setFigurineId(awarded.getId());
            userFigurine.setBookId(bookId);
            userFigurine.setEarnedAt(new Date());
            userFigurineRepository.save(userFigurine);
        }
    }

    // Получить все достижения пользователя с данными о них (для отображения на полке)
    public List<UserAchievement> getUserAchievements(String userId) {
        return userAchievementRepository.findByUserId(userId);
    }

    // Получить все фигурки пользователя
    public List<UserFigurine> getUserFigurines(String userId) {
        return userFigurineRepository.findByUserId(userId);
    }
}