package com.example.redis;

public class RedisKeys {

    // Virality score
    public static String viralityScore(Long postId) {
        return "post:" + postId + ":virality_score";
    }

    // Bot reply count
    public static String botCount(Long postId) {
        return "post:" + postId + ":bot_count";
    }

    // Cooldown
    public static String cooldown(Long botId, Long userId) {
        return "cooldown:bot_" + botId + ":user_" + userId;
    }

    // Notification queue
    public static String pendingNotifications(Long userId) {
        return "user:" + userId + ":pending_notifs";
    }

    // Notification cooldown
    public static String notificationCooldown(Long userId) {
        return "user:" + userId + ":notif_cooldown";
    }
}