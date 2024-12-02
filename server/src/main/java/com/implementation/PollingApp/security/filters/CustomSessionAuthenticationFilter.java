package com.implementation.PollingApp.security.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.implementation.PollingApp.security.authObjects.CustomSessionAuthenticationObject;
import com.implementation.PollingApp.security.managers.CustomSessionAuthenticationManager;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSessionAuthenticationFilter extends OncePerRequestFilter {

        @Autowired
        private CustomSessionAuthenticationManager manager;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

                var authenticationObject = new CustomSessionAuthenticationObject(request);

                var authenticationResult = manager.authenticate(authenticationObject);

                if (authenticationResult.isAuthenticated()) {
                        SecurityContextHolder.getContext().setAuthentication(authenticationResult);
                }

                filterChain.doFilter(request, response);
        }
}
