package com.playbook.shelf_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document(collection = "shelves")
@Data
public class Shelf {
    @Id
    private String id;
    private String userId;
    private String name;
    private boolean isPublic = false;   // публичная полка
    private List<CompletedBook> books;   // книги, которые пользователь прочитал
    private Date createdAt;
    private Date updatedAt;
}