package com.playbook.group_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "groups")
@Data
public class Group {
    @Id
    private String id;
    private String name;
    private String inviteCode;          // уникальный код для приглашения
    private String adminUserId;
    private List<String> memberUserIds = new ArrayList<>();
    private AssignedBook assignedBook;   // текущая назначенная книга (если есть)
    private List<CompletedAssignment> completedAssignments = new ArrayList<>();
    private Date createdAt;
    private Date updatedAt;
}