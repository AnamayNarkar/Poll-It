package com.implementation.PollingApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.implementation.PollingApp.entity.FeedValueEntity;
import com.implementation.PollingApp.entity.SessionValueEntity;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, SessionValueEntity> sessionRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, SessionValueEntity> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(SessionValueEntity.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, FeedValueEntity> feedRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, FeedValueEntity> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(FeedValueEntity.class));
        return template;
    }

}