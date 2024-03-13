package com.example.project.model.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Post {
    private String id;
    private String title;
    private String body;
    private String userId;
}
