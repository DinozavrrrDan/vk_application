package com.example.project.model.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comment {
    private String id;
    private String postId;
    private String name;
    private String email;
    private String body;
}
