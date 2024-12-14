package com.implementation.PollingApp.repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.implementation.PollingApp.entity.FeedValueEntity;

@Service
public class RedisRepositoryForFeed {

    private final RedisTemplate<String, FeedValueEntity> redisTemplate;

    @Autowired
    public RedisRepositoryForFeed(RedisTemplate<String, FeedValueEntity> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String generateFeedId() {
        return UUID.randomUUID().toString();
    }

    public String saveValue(FeedValueEntity value, long timeout) {
        String key = generateFeedId();
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
        return key;
    }

    public FeedValueEntity getValue(String key) {
        FeedValueEntity value = redisTemplate.opsForValue().get(key);
        if (value == null)
            return null;
        else
            return value;
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    public void updateValue(String key, FeedValueEntity value) {
        Long timeout = redisTemplate.getExpire(key);
        redisTemplate.opsForValue().set(key, value, TimeUnit.SECONDS.toMinutes(timeout), TimeUnit.MINUTES);
    }

}
