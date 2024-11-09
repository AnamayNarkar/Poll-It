package com.implementation.JournalApp.security.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.implementation.JournalApp.security.authObjects.CustomSessionAuthenticationObject;
import com.implementation.JournalApp.security.managers.CustomSessionAuthenticationManager;

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

                var authObject = new CustomSessionAuthenticationObject(request);

                var responsehehe = manager.authenticate(authObject);

                if (responsehehe.isAuthenticated()) {
                        System.out.println(responsehehe);
                        SecurityContextHolder.getContext().setAuthentication(responsehehe);
                        System.out.println(SecurityContextHolder.getContext().getAuthentication());
                }

                System.out.println(SecurityContextHolder.getContext().getAuthentication());

                filterChain.doFilter(request, response);
        }
}