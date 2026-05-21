package com.playbook.group_service.dto;

import lombok.Data;
import java.util.Date;

@Data
public class AssignBookRequest {
    private String bookId;
    private String bookTitle;
    private Date deadline;
}