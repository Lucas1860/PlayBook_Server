package com.playbook.progress_service.service;

import com.playbook.progress_service.dto.AnswerEvent;
import com.playbook.progress_service.dto.ProgressResponse;
import com.playbook.progress_service.entity.UserReadingProgress;
import com.playbook.progress_service.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadingProgressService {

    private final UserProgressRepository progressRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gamification.service.url}")
    private String gamificationServiceUrl;

    @Value("${book.service.url}")
    private String bookServiceUrl;

    // Вспомогательный метод для получения общего количества вопросов по книге (кэшируем или вызываем BookService)
    private int getTotalQuestionsCount(String bookId) {
        // Здесь можно сделать вызов к BookService: GET /books/{bookId}/questions/count
        // Для упрощения будем хранить totalQuestions в самом прогресс-записи, но при первом создании нужно узнать.
        // Реализуем через вызов REST.
        try {
            Integer count = restTemplate.getForObject(bookServiceUrl + "/books/" + bookId + "/questions/count", Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    // Начало чтения книги (создание записи прогресса, если её нет)
    public UserReadingProgress startReading(String userId, String bookId) {
        Optional<UserReadingProgress> existing = progressRepository.findByUserIdAndBookId(userId, bookId);
        if (existing.isPresent()) {
            return existing.get();
        }
        int totalQuestions = getTotalQuestionsCount(bookId);
        UserReadingProgress progress = new UserReadingProgress();
        progress.setUserId(userId);
        progress.setBookId(bookId);
        progress.setTotalQuestions(totalQuestions);
        progress.setAnsweredQuestionIds(new HashSet<>());
        progress.setTimeSpentSeconds(0);
        progress.setCompleted(false);
        progress.setLastActivityAt(new Date());
        progress.setCharacterScores(new HashMap<>());
        progress.setAchievementScores(new HashMap<>());
        return progressRepository.save(progress);
    }

    // Обновление времени чтения (фронтенд шлёт пульс каждые N секунд)
    public void updateTimeSpent(String userId, String bookId, long additionalSeconds) {
        UserReadingProgress progress = progressRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("Progress not found"));
        progress.setTimeSpentSeconds(progress.getTimeSpentSeconds() + additionalSeconds);
        progress.setLastActivityAt(new Date());
        progressRepository.save(progress);
    }

    // Обработка ответа на вопрос
    public ProgressResponse submitAnswer(AnswerEvent answer) {
        String userId = answer.getUserId();
        String bookId = answer.getBookId();
        String questionId = answer.getQuestionId();

        UserReadingProgress progress = progressRepository.findByUserIdAndBookId(userId, bookId)
                .orElseGet(() -> startReading(userId, bookId));

        // Если вопрос уже отвечен – игнорируем
        if (progress.getAnsweredQuestionIds().contains(questionId)) {
            return toProgressResponse(progress);
        }

        // Добавляем веса
        Map<String, Integer> newCharScores = new HashMap<>(progress.getCharacterScores());
        Map<String, Integer> newAchieveScores = new HashMap<>(progress.getAchievementScores());

        answer.getCharacterWeights().forEach((key, val) ->
            newCharScores.merge(key, val, Integer::sum)
        );
        answer.getAchievementTags().forEach((key, val) ->
            newAchieveScores.merge(key, val, Integer::sum)
        );

        progress.setCharacterScores(newCharScores);
        progress.setAchievementScores(newAchieveScores);
        progress.getAnsweredQuestionIds().add(questionId);
        progress.setLastActivityAt(new Date());

        boolean wasCompleted = progress.isCompleted();
        boolean nowCompleted = progress.getAnsweredQuestionIds().size() >= progress.getTotalQuestions();
        if (nowCompleted && !wasCompleted) {
            progress.setCompleted(true);
            progress.setCompletedAt(new Date());
            // Уведомить Gamification о завершении книги (выдача фигурки)
            notifyBookCompleted(userId, bookId, progress.getCharacterScores());
        }

        UserReadingProgress saved = progressRepository.save(progress);
        // Уведомить Gamification о проверке достижений (после каждого ответа)
        notifyAchievementCheck(userId, bookId, progress.getAchievementScores());

        return toProgressResponse(saved);
    }

    private void notifyAchievementCheck(String userId, String bookId, Map<String, Integer> achievementScores) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", userId);
            payload.put("bookId", bookId);
            payload.put("achievementScores", achievementScores);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            restTemplate.postForEntity(gamificationServiceUrl + "/gamification/check-achievements", entity, Void.class);
        } catch (Exception e) {
            // логируем ошибку, но не прерываем
            e.printStackTrace();
        }
    }

    private void notifyBookCompleted(String userId, String bookId, Map<String, Integer> characterScores) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", userId);
            payload.put("bookId", bookId);
            payload.put("characterScores", characterScores);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            restTemplate.postForEntity(gamificationServiceUrl + "/gamification/award-figurine", entity, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ProgressResponse toProgressResponse(UserReadingProgress progress) {
        return new ProgressResponse(
                progress.getUserId(),
                progress.getBookId(),
                progress.getAnsweredQuestionIds().size(),
                progress.getTotalQuestions(),
                (int) ((double) progress.getAnsweredQuestionIds().size() / progress.getTotalQuestions() * 100),
                progress.getTimeSpentSeconds(),
                progress.isCompleted(),
                progress.getCompletedAt()
        );
    }

    // Получение прогресса пользователя по книге
    public ProgressResponse getProgress(String userId, String bookId) {
        UserReadingProgress progress = progressRepository.findByUserIdAndBookId(userId, bookId)
                .orElse(null);
        if (progress == null) {
            return new ProgressResponse(userId, bookId, 0, 0, 0, 0, false, null);
        }
        return toProgressResponse(progress);
    }

    // Получение прогресса для множества пользователей по одной книге (для группы)
    public List<ProgressResponse> getProgressForUsers(List<String> userIds, String bookId) {
        return userIds.stream()
                .map(uid -> getProgress(uid, bookId))
                .collect(Collectors.toList());
    }
}