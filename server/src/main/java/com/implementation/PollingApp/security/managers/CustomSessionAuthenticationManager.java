package com.implementation.PollingApp.security.managers;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.implementation.PollingApp.security.providers.CustomSessionAuthenticationProvider;

@Component
@AllArgsConstructor
public class CustomSessionAuthenticationManager implements AuthenticationManager {

    @Autowired
    private CustomSessionAuthenticationProvider provider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (provider.supports(authentication.getClass())) {
            var testvar = provider.authenticate(authentication);
            return testvar;
        }

        throw new BadCredentialsException("Oh No!");
    }
}