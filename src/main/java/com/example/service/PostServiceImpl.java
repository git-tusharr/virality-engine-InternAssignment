package com.example.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.CommentRequest;
import com.example.dto.PostRequest;
import com.example.entity.Comment;
import com.example.entity.Post;
import com.example.repository.CommentRepository;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import com.example.redis.RedisGuardService;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisGuardService redisGuardService;

    @Autowired
    private ViralityService viralityService;

    @Autowired
    private NotificationService notificationService;

    // CREATE POST
    @Override
    public Post createPost(PostRequest request) {

        Post post = new Post();

        post.setAuthorId(request.getAuthorId());
        post.setIsBot(request.getIsBot());
        post.setContent(request.getContent());
        post.setCreatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    // ADD COMMENT
    @Transactional
    @Override
    public Comment addComment(Long postId,
                              CommentRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        // Vertical Cap (for all comments)
        redisGuardService.checkDepthLevel(
                request.getDepthLevel()
        );

        // BOT LOGIC
        if (Boolean.TRUE.equals(request.getIsBot())) {

            // Horizontal Cap
            redisGuardService.checkBotReplyLimit(postId);

            // Cooldown Cap
            redisGuardService.checkCooldown(
                    request.getAuthorId(),
                    post.getAuthorId()
            );

            // Virality +1
            viralityService.addBotReplyScore(postId);

            // Notification Engine
            notificationService.handleBotNotification(
                    post.getAuthorId(),
                    "Bot replied to your post"
            );

        } else {

            // Human Comment +50
            viralityService.addHumanCommentScore(postId);
        }

        Comment comment = new Comment();

        comment.setPostId(postId);
        comment.setAuthorId(request.getAuthorId());
        comment.setIsBot(request.getIsBot());
        comment.setContent(request.getContent());
        comment.setDepthLevel(request.getDepthLevel());
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    // LIKE POST
    @Override
    public String likePost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        // Human Like +20
        viralityService.addLikeScore(postId);

        return "Post liked successfully";
    }
}