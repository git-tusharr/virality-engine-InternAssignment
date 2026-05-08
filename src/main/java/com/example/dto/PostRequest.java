package com.example.dto;

import lombok.Data;

@Data
public class PostRequest {

    private Long authorId;

    private Boolean isBot;

    private String content;
}