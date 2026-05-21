package com.playbook.group_service.entity;


import lombok.Data;
import java.util.Date;

@Data
public class AssignedBook {
    private String bookId;
    private String bookTitle;
    private Date deadline;
    private Date assignedAt;
}