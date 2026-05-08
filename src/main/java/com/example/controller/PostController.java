package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.CommentRequest;
import com.example.dto.PostRequest;
import com.example.entity.Comment;
import com.example.entity.Post;
import com.example.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // CREATE POST
    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestBody PostRequest request) {

        return ResponseEntity.ok(
                postService.createPost(request)
        );
    }

    // ADD COMMENT
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request) {

        return ResponseEntity.ok(
                postService.addComment(postId, request)
        );
    }

    // LIKE POST
    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(
            @PathVariable Long postId) {

        return ResponseEntity.ok(
                postService.likePost(postId)
        );
    }
}