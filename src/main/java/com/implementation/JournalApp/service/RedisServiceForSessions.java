package com.implementation.JournalApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import com.implementation.JournalApp.entity.SessionValueEntity;

@Service
public class RedisServiceForSessions {

        private final RedisTemplate<String, SessionValueEntity> redisTemplate;

        @Autowired
        public RedisServiceForSessions(RedisTemplate<String, SessionValueEntity> redisTemplate) {
                this.redisTemplate = redisTemplate;
        }

        public void saveValue(String key, SessionValueEntity value, long timeout) {
                redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
        }

        public SessionValueEntity getValue(String key) {
                return redisTemplate.opsForValue().get(key);
        }

        public void deleteValue(String key) {
                redisTemplate.delete(key);
        }
}