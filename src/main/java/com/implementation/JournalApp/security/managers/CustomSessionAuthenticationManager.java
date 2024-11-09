package com.implementation.JournalApp.security.managers;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.implementation.JournalApp.security.providers.CustomSessionAuthenticationProvider;

@Component
@AllArgsConstructor
public class CustomSessionAuthenticationManager implements AuthenticationManager {

        @Autowired
        private CustomSessionAuthenticationProvider provider;

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                if (provider.supports(authentication.getClass())) {
                        return provider.authenticate(authentication);
                }

                throw new BadCredentialsException("Oh No!");
        }
}