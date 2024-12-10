package com.implementation.PollingApp.util;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.implementation.PollingApp.entity.SessionValueEntity;
import com.implementation.PollingApp.entity.UserEntity;
import com.implementation.PollingApp.exception.custom.InternalServerErrorException;
import com.implementation.PollingApp.repository.RedisRepositoryForFeed;
import com.implementation.PollingApp.repository.RedisRepositoryForSessions;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SessionUtils {

        @Autowired
        private RedisRepositoryForSessions redisRepositoryForSessions;

        @Autowired
        private RedisRepositoryForFeed redisRepositoryForFeed;

        private String generateSessionId() {
                return UUID.randomUUID().toString();
        }

        private void setSessionIdCookie(HttpServletResponse response, String sessionId) {
                Cookie cookie = new Cookie("SESSION_ID", sessionId);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(30 * 60); // 30 minutes
                response.addCookie(cookie);
        }

        private String getSessionIdFromCookie(Cookie[] cookies) {
                if (cookies != null) {
                        for (Cookie cookie : cookies) {
                                if (cookie.getName().equals("SESSION_ID")) {
                                        return cookie.getValue();
                                }
                        }
                }
                return null;
        }

        private Cookie[] getCookiesFromRequest(HttpServletRequest request) {
                return request.getCookies();
        }

        public String getSessionIdFromRequest(HttpServletRequest request) {
                return getSessionIdFromCookie(getCookiesFromRequest(request));
        }

        private void saveSessionValueInRedis(String sessionId, SessionValueEntity sessionValueEntity) {
                redisRepositoryForSessions.saveValue(sessionId, sessionValueEntity, 30);
        }

        private SessionValueEntity getSessionValueFromRedis(String sessionId) {
                return redisRepositoryForSessions.getValue(sessionId);
        }

        private void deleteSessionValueFromRedis(String sessionId) {
                redisRepositoryForSessions.deleteValue(sessionId);

        }

        // Bigger Methods that use the above methods

        public void saveUserSession(HttpServletResponse response, UserEntity userEntity) {
                String sessionId = generateSessionId();
                SessionValueEntity sessionValueEntity = new SessionValueEntity(userEntity.getId().toHexString(), userEntity.getUsername(), userEntity.getRoles(), null);
                setSessionIdCookie(response, sessionId);
                saveSessionValueInRedis(sessionId, sessionValueEntity);
        }

        public SessionValueEntity getUserSession(HttpServletRequest request) {
                String sessionId = getSessionIdFromRequest(request);
                if (sessionId != null) {
                        return getSessionValueFromRedis(sessionId);
                }
                return null;
        }

        public void clearUserSessionAndRemoveCookie(HttpServletResponse response, HttpServletRequest request) {
                String sessionId = getSessionIdFromRequest(request);
                if (sessionId != null) {
                        SessionValueEntity sve = redisRepositoryForSessions.getValue(sessionId);
                        if (sve != null) {
                                if (redisRepositoryForFeed.getValue(sve.getFeedStateRedisKey()) != null) {
                                        redisRepositoryForFeed.deleteValue(sve.getFeedStateRedisKey());
                                }
                        }

                        deleteSessionValueFromRedis(sessionId);
                        Cookie cookie = new Cookie("SESSION_ID", null);
                        cookie.setHttpOnly(true);
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                } else {
                        throw new InternalServerErrorException("Error logging out user");
                }
        }

        public SessionValueEntity validateSession(HttpServletRequest request) {
                String sessionId = getSessionIdFromRequest(request);
                if (sessionId != null) {
                        return getSessionValueFromRedis(sessionId);
                }
                return null;
        }
}