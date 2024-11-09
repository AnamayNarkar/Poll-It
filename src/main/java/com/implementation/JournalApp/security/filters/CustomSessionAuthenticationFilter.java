package com.implementation.JournalApp.security.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.implementation.JournalApp.security.authObjects.CustomSessionAuthenticationObject;
import com.implementation.JournalApp.security.managers.CustomSessionAuthenticationManager;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomSessionAuthenticationFilter extends OncePerRequestFilter {

        @Autowired
        private CustomSessionAuthenticationManager manager;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

                var authObject = new CustomSessionAuthenticationObject(request);

                manager.authenticate(authObject);

                filterChain.doFilter(request, response);
        }
}