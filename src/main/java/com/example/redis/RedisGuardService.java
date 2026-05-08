package com.example.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exception.TooManyRequestsException;
import com.example.service.RedisService;

@Service
public class RedisGuardService {

    @Autowired
    private RedisService redisService;

    // Horizontal Cap
    public void checkBotReplyLimit(Long postId) {

        String key = RedisKeys.botCount(postId);

        Long count = redisService.increment(key);

        if (count > 100) {
            throw new TooManyRequestsException(
                    "Bot reply limit exceeded for this post");
        }
    }

    // Vertical Cap
    public void checkDepthLevel(Integer depthLevel) {

        if (depthLevel > 20) {
            throw new TooManyRequestsException(
                    "Comment depth exceeded");
        }
    }

    // Cooldown Cap
    public void checkCooldown(Long botId, Long userId) {

        String key = RedisKeys.cooldown(botId, userId);

        Boolean exists = redisService.hasKey(key);

        if (Boolean.TRUE.equals(exists)) {
            throw new TooManyRequestsException(
                    "Bot cooldown active");
        }

        redisService.setValueWithTimeout(
                key,
                "ACTIVE",
                10,
                TimeUnit.MINUTES
        );
    }
}