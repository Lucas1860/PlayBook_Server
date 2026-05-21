package com.playbook.book_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "figurines")
@Data
public class Figurine {
    @Id
    private String id;
    private String bookId;
    private String characterId;   // grinev, masha, shvabrin, pugachev, neutral
    private String name;
    private String description;
    private String imageUrl;
}
