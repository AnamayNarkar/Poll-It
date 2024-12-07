package com.implementation.PollingApp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import com.implementation.PollingApp.entity.SessionValueEntity;

@Service
public class RedisRepositoryForSessions {

        private final RedisTemplate<String, SessionValueEntity> redisTemplate;

        @Autowired
        public RedisRepositoryForSessions(RedisTemplate<String, SessionValueEntity> redisTemplate) {
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

        public void updateValue(String key, SessionValueEntity value) {
                Long remainingTtl = redisTemplate.getExpire(key);
                redisTemplate.opsForValue().set(key, value, remainingTtl, TimeUnit.MINUTES);
        }

}