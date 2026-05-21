package com.playbook.group_service.entity;

import lombok.Data;

@Data
public class MemberSnapshot1 {
    private String userId;
    private int progressPercent;
    private Long timeSpentSeconds;
}