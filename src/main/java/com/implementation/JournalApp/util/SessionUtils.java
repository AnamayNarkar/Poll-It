package com.implementation.JournalApp.util;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.implementation.JournalApp.service.RedisServiceForSessions;
import com.implementation.JournalApp.entity.SessionValueEntity;
import com.implementation.JournalApp.entity.UserEntity;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SessionUtils {

        @Autowired
        private RedisServiceForSessions redisServiceForSessions;

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

        private String getSessionIfFromCookie(Cookie[] cookies) {
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

        private String getSessionIdFromRequest(HttpServletRequest request) {
                return getSessionIfFromCookie(getCookiesFromRequest(request));
        }

        private void saveSessionValueInRedis(String sessionId, SessionValueEntity sessionValueEntity) {
                redisServiceForSessions.saveValue(sessionId, sessionValueEntity, 30);
        }

        private SessionValueEntity getSessionValueFromRedis(String sessionId) {
                return redisServiceForSessions.getValue(sessionId);
        }

        private void deleteSessionValueFromRedis(String sessionId) {
                redisServiceForSessions.deleteValue(sessionId);
        }

        // Bigger Methods that use the above methods

        public void saveUserSession(HttpServletResponse response, UserEntity userEntity) {
                String sessionId = generateSessionId();
                SessionValueEntity sessionValueEntity = new SessionValueEntity(userEntity.getId(),
                                userEntity.getUsername(),
                                userEntity.getRoles());
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
                        deleteSessionValueFromRedis(sessionId);
                        Cookie cookie = new Cookie("SESSION_ID", null);
                        cookie.setHttpOnly(true);
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                }
        }
}
