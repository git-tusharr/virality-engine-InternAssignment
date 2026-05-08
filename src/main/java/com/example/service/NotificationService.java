package com.example.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.redis.RedisKeys;

@Service
public class NotificationService {

    @Autowired
    private RedisService redisService;

    public void handleBotNotification(
            Long userId,
            String message
    ) {

        String cooldownKey =
                RedisKeys.notificationCooldown(userId);

        Boolean exists =
                redisService.hasKey(cooldownKey);

        // If cooldown active → push to queue
        if (Boolean.TRUE.equals(exists)) {

            redisService.pushToList(
                    RedisKeys.pendingNotifications(userId),
                    message
            );

        } else {

            // Send immediate notification
            System.out.println(
                    "Push Notification Sent to User: "
                            + message
            );

            // Start cooldown
            redisService.setValueWithTimeout(
                    cooldownKey,
                    "ACTIVE",
                    15,
                    TimeUnit.MINUTES
            );
        }
    }
}