package com.example.service;

import com.example.dto.CommentRequest;
import com.example.dto.PostRequest;
import com.example.entity.Comment;
import com.example.entity.Post;

public interface PostService {

    Post createPost(PostRequest request);

    Comment addComment(Long postId, CommentRequest request);

    String likePost(Long postId);
}