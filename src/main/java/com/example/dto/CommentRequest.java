package com.example.dto;

import lombok.Data;

@Data
public class CommentRequest {

    private Long authorId;

    private Boolean isBot;

    private String content;

    private Integer depthLevel;
}