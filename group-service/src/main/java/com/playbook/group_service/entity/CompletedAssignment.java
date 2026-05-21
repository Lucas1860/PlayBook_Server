package com.playbook.group_service.entity;

import lombok.Data;
import java.util.Date;
import java.util.List;


@Data
public class CompletedAssignment {
    private String bookId;
    private String bookTitle;
    private Date deadline;
    private Date completedAt;
    private List<MemberSnapshot1> memberSnapshots; // срез результатов на момент завершения
}

@Data
class MemberSnapshot {
    private String userId;
    private int progressPercent;
    private Long timeSpentSeconds;
}

