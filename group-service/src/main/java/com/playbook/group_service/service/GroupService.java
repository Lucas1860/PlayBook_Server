package com.playbook.group_service.service;


import com.playbook.group_service.dto.*;
import com.playbook.group_service.entity.AssignedBook;
import com.playbook.group_service.entity.CompletedAssignment;
import com.playbook.group_service.entity.Group;
import com.playbook.group_service.entity.MemberSnapshot1;
import com.playbook.group_service.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String INVITE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    @Value("${progress.service.url}")
    private String progressServiceUrl;

    @Value("${book.service.url}")
    private String bookServiceUrl;

    private String generateInviteCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(INVITE_CHARS.charAt(random.nextInt(INVITE_CHARS.length())));
        }
        return sb.toString();
    }

    // Создание группы
    public Group createGroup(CreateGroupRequest request, String adminUserId) {
        Group group = new Group();
        group.setName(request.getName());
        group.setAdminUserId(adminUserId);
        group.setMemberUserIds(new ArrayList<>());
        group.getMemberUserIds().add(adminUserId);
        group.setInviteCode(generateInviteCode());
        group.setCreatedAt(new Date());
        group.setUpdatedAt(new Date());
        group.setAssignedBook(null);
        group.setCompletedAssignments(new ArrayList<>());
        return groupRepository.save(group);
    }

    // Вступление в группу по коду
    public Group joinGroup(String inviteCode, String userId) {
        Group group = groupRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        if (!group.getMemberUserIds().contains(userId)) {
            group.getMemberUserIds().add(userId);
            group.setUpdatedAt(new Date());
            groupRepository.save(group);
        }
        return group;
    }

    // Выход из группы
    public Group leaveGroup(String groupId, String userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        if (group.getAdminUserId().equals(userId)) {
            throw new RuntimeException("Администратор не может выйти из группы. Удалите группу или передайте права.");
        }
        group.getMemberUserIds().remove(userId);
        group.setUpdatedAt(new Date());
        return groupRepository.save(group);
    }

    // Исключение участника (только админ)
    public Group removeMember(String groupId, String adminUserId, String targetUserId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        if (!group.getAdminUserId().equals(adminUserId)) {
            throw new RuntimeException("Только администратор может исключать участников");
        }
        group.getMemberUserIds().remove(targetUserId);
        group.setUpdatedAt(new Date());
        return groupRepository.save(group);
    }

    // Назначить книгу группе (админ)
    public Group assignBook(String groupId, String adminUserId, AssignBookRequest request) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        if (!group.getAdminUserId().equals(adminUserId)) {
            throw new RuntimeException("Только администратор может назначать книги");
        }
        // Проверим, что книга существует (опционально через book-service)
        AssignedBook assigned = new AssignedBook();
        assigned.setBookId(request.getBookId());
        assigned.setBookTitle(request.getBookTitle());
        assigned.setDeadline(request.getDeadline());
        assigned.setAssignedAt(new Date());
        group.setAssignedBook(assigned);
        group.setUpdatedAt(new Date());
        return groupRepository.save(group);
    }

    // Удалить назначенную книгу (админ)
    public Group removeAssignedBook(String groupId, String adminUserId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        if (!group.getAdminUserId().equals(adminUserId)) {
            throw new RuntimeException("Только администратор может удалять назначенную книгу");
        }
        group.setAssignedBook(null);
        group.setUpdatedAt(new Date());
        return groupRepository.save(group);
    }

    // Завершить дедлайн (админ) – переместить текущую книгу в completedAssignments
    public Group completeDeadline(String groupId, String adminUserId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        if (!group.getAdminUserId().equals(adminUserId)) {
            throw new RuntimeException("Только администратор может завершить дедлайн");
        }
        AssignedBook assigned = group.getAssignedBook();
        if (assigned == null) {
            throw new RuntimeException("Нет назначенной книги");
        }
        // Получить прогресс каждого участника на текущий момент
        List<String> memberIds = group.getMemberUserIds();
        // Вызов progress-service для получения прогресса по книге
        List<ProgressResponse> progresses = fetchProgressForMembers(memberIds, assigned.getBookId());
        List<MemberSnapshot1> snapshots = progresses.stream()
                .map(p -> {
                    MemberSnapshot1 s = new MemberSnapshot1();
                    s.setUserId(p.getUserId());
                    s.setProgressPercent(p.getPercentComplete());
                    s.setTimeSpentSeconds(p.getTimeSpentSeconds());
                    return s;
                })
                .collect(Collectors.toList());

        CompletedAssignment completed = new CompletedAssignment();
        completed.setBookId(assigned.getBookId());
        completed.setBookTitle(assigned.getBookTitle());
        completed.setDeadline(assigned.getDeadline());
        completed.setCompletedAt(new Date());
        completed.setMemberSnapshots(snapshots);

        group.getCompletedAssignments().add(completed);
        group.setAssignedBook(null);
        group.setUpdatedAt(new Date());
        return groupRepository.save(group);
    }

    private List<ProgressResponse> fetchProgressForMembers(List<String> memberIds, String bookId) {
        // POST /progress/batch?bookId=... с телом memberIds
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> entity = new HttpEntity<>(memberIds, headers);
        ProgressResponse[] responses = restTemplate.postForObject(
                progressServiceUrl + "/progress/batch?bookId=" + bookId,
                entity,
                ProgressResponse[].class
        );
        return responses != null ? Arrays.asList(responses) : List.of();
    }

    // Удалить группу (админ)
    public void deleteGroup(String groupId, String adminUserId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        if (!group.getAdminUserId().equals(adminUserId)) {
            throw new RuntimeException("Только администратор может удалить группу");
        }
        groupRepository.delete(group);
    }

    // Получить информацию о группе (с проверкой членства)
    public Group getGroup(String groupId, String userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        if (!group.getMemberUserIds().contains(userId)) {
            throw new RuntimeException("Вы не являетесь участником этой группы");
        }
        return group;
    }

    // Получить все группы, где состоит пользователь
    public List<Group> getUserGroups(String userId) {
        return groupRepository.findByMemberUserIdsContaining(userId);
    }
}