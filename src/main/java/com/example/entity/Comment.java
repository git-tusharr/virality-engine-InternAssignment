package com.example.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;

    private Long authorId;

    private Boolean isBot;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer depthLevel;

    private LocalDateTime createdAt;
}