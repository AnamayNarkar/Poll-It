package com.implementation.PollingApp.util;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.implementation.PollingApp.entity.SessionValueEntity;
import com.implementation.PollingApp.entity.UserEntity;
import com.implementation.PollingApp.exception.custom.InternalServerErrorException;
import com.implementation.PollingApp.exception.custom.SessionNotFoundException;
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

        private String getFeedIdFromSession(SessionValueEntity sve) {
                if (sve == null) {
                        throw new SessionNotFoundException("Session value entity is null.");
                }
                return sve.getFeedStateRedisKey();
        }

        private void setSessionIdCookie(HttpServletResponse response, String sessionId) {
                try {
                        Cookie cookie = new Cookie("SESSION_ID", sessionId);
                        cookie.setHttpOnly(true);
                        cookie.setPath("/");
                        cookie.setMaxAge(30 * 60); // 30 minutes
                        response.addCookie(cookie);
                } catch (Exception e) {
                        throw new InternalServerErrorException("Failed to set session ID cookie.");
                }
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
                try {
                        return request.getCookies();
                } catch (Exception e) {
                        throw new InternalServerErrorException("Failed to retrieve cookies from request.");
                }
        }

        public String getSessionIdFromRequest(HttpServletRequest request) {
                try {
                        return getSessionIdFromCookie(getCookiesFromRequest(request));
                } catch (Exception e) {
                        throw new InternalServerErrorException("Failed to retrieve session ID from request.");
                }
        }

        private void saveSessionValueInRedis(String sessionId, SessionValueEntity sessionValueEntity) {
                try {
                        redisRepositoryForSessions.saveValue(sessionId, sessionValueEntity, 30);
                } catch (Exception e) {
                        throw new InternalServerErrorException("Failed to save session value in Redis.");
                }
        }

        private SessionValueEntity getSessionValueFromRedis(String sessionId) {
                try {
                        SessionValueEntity sessionValue = redisRepositoryForSessions.getValue(sessionId);
                        if (sessionValue == null) {
                                throw new SessionNotFoundException("Session not found in Redis.");
                        }
                        return sessionValue;
                } catch (Exception e) {
                        throw new InternalServerErrorException("Failed to retrieve session value from Redis.");
                }
        }

        private void deleteSessionValueFromRedis(String sessionId) {
                try {
                        redisRepositoryForSessions.deleteValue(sessionId);
                } catch (Exception e) {
                        throw new InternalServerErrorException("Failed to delete session value from Redis.");
                }
        }

        public void saveUserSession(HttpServletResponse response, UserEntity userEntity) {
                try {
                        String sessionId = generateSessionId();
                        SessionValueEntity sessionValueEntity = new SessionValueEntity(userEntity.getId().toHexString(), userEntity.getUsername(), userEntity.getRoles(), null);
                        setSessionIdCookie(response, sessionId);
                        saveSessionValueInRedis(sessionId, sessionValueEntity);
                } catch (Exception e) {
                        throw new InternalServerErrorException("Failed to save user session.");
                }
        }

        public SessionValueEntity getUserSession(HttpServletRequest request) {
                try {
                        String sessionId = getSessionIdFromRequest(request);
                        if (sessionId != null) {
                                return getSessionValueFromRedis(sessionId);
                        }
                        throw new SessionNotFoundException("Session ID not found in request.");
                } catch (Exception e) {
                        throw new InternalServerErrorException("Failed to retrieve user session.");
                }
        }

        public void clearUserSessionAndRemoveCookie(HttpServletResponse response, HttpServletRequest request) {
                try {
                        String sessionId = getSessionIdFromRequest(request);
                        if (sessionId != null) {
                                SessionValueEntity sve = redisRepositoryForSessions.getValue(sessionId);
                                if (sve != null) {
                                        redisRepositoryForFeed.deleteValue(getFeedIdFromSession(sve));
                                }
                                deleteSessionValueFromRedis(sessionId);
                                Cookie cookie = new Cookie("SESSION_ID", null);
                                cookie.setHttpOnly(true);
                                cookie.setPath("/");
                                cookie.setMaxAge(0);
                                response.addCookie(cookie);
                        } else {
                                throw new SessionNotFoundException("Session ID not found during logout.");
                        }
                } catch (Exception e) {
                        throw new InternalServerErrorException("Error occurred while logging out user.");
                }
        }

        public SessionValueEntity validateSession(HttpServletRequest request) {
                try {
                        String sessionId = getSessionIdFromRequest(request);
                        if (sessionId != null) {
                                return getSessionValueFromRedis(sessionId);
                        }
                        throw new SessionNotFoundException("Session ID not found in request.");
                } catch (Exception e) {
                        throw new InternalServerErrorException("Failed to validate session.");
                }
        }

}