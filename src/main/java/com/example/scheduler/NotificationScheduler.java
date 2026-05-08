package com.example.scheduler;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Runs every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void processNotifications() {

        Set<String> keys =
                redisTemplate.keys("user:*:pending_notifs");

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {

            Long size =
                    redisTemplate.opsForList().size(key);

            if (size != null && size > 0) {

                System.out.println(
                        "Summarized Push Notification: "
                                + size
                                + " new interactions"
                );

                // Clear notifications
                redisTemplate.delete(key);
            }
        }
    }
}