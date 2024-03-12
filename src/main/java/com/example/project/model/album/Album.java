package com.example.project.model.album;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Album {
    private String userId;
    private String id;
    private String title;
}
