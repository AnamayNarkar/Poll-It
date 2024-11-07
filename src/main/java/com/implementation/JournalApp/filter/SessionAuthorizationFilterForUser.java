package com.implementation.JournalApp.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.implementation.JournalApp.entity.SessionValueEntity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.implementation.JournalApp.exception.custom.InvalidSessionException;
import com.implementation.JournalApp.exception.custom.UnauthorizedRequestException;
import com.implementation.JournalApp.util.SessionUtils;

public class SessionAuthorizationFilterForUser extends OncePerRequestFilter {

        private String requiredRole = "USER";

        @Autowired
        private SessionUtils sessionUtils;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

                SessionValueEntity sessionValue = sessionUtils.getUserSession(request);
                if (sessionValue == null) {
                        throw new InvalidSessionException("Invalid session or session expired");
                }

                if (sessionValue.getRoles() == null || sessionValue.getRoles().size() == 0 || !sessionValue.getRoles().contains(requiredRole)) {
                        throw new UnauthorizedRequestException("You do not have the required role to access this resource");
                }

                filterChain.doFilter(request, response);
        }
}