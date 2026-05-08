package com.example.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Increment counter
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    // Get value
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Set value
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Set with TTL
    public void setValueWithTimeout(String key, Object value,
                                    long timeout, TimeUnit unit) {

        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // Check exists
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    // Delete key
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    // Push to list
    public void pushToList(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    // Get list size
    public Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }
}