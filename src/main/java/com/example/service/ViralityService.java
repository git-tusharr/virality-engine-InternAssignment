package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.redis.RedisKeys;

@Service
public class ViralityService {

    @Autowired
    private RedisService redisService;

    // Bot Reply = +1
    public void addBotReplyScore(Long postId) {

        String key = RedisKeys.viralityScore(postId);

        redisService.increment(key);
    }

    // Human Like = +20
    public void addLikeScore(Long postId) {

        String key = RedisKeys.viralityScore(postId);

        for (int i = 0; i < 20; i++) {
            redisService.increment(key);
        }
    }

    // Human Comment = +50
    public void addHumanCommentScore(Long postId) {

        String key = RedisKeys.viralityScore(postId);

        for (int i = 0; i < 50; i++) {
            redisService.increment(key);
        }
    }

    // Get Score
    public Object getViralityScore(Long postId) {

        return redisService.getValue(
                RedisKeys.viralityScore(postId)
        );
    }
}