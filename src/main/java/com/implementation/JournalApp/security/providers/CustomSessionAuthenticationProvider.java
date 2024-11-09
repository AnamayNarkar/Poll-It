package com.implementation.JournalApp.security.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.implementation.JournalApp.entity.SessionValueEntity;
import com.implementation.JournalApp.security.authObjects.CustomSessionAuthenticationObject;
import com.implementation.JournalApp.util.SessionUtils;

@Component
public class CustomSessionAuthenticationProvider implements AuthenticationProvider {

        @Autowired
        private SessionUtils sessionUtils;

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                CustomSessionAuthenticationObject ca = (CustomSessionAuthenticationObject) authentication;

                // Retrieve session based on request object
                SessionValueEntity sve = sessionUtils.getUserSession(ca.getRequest());

                // If session is invalid or null, mark the authentication as failed
                if (sve == null) {
                        CustomSessionAuthenticationObject failedAuth = new CustomSessionAuthenticationObject(null, null, null, null);
                        failedAuth.setAuthenticated(false);
                        return failedAuth;
                }

                // Otherwise, set authenticated to true and return an authenticated object
                CustomSessionAuthenticationObject authenticatedObject = new CustomSessionAuthenticationObject(ca.getRequest(), sve.getId(), sve.getUsername(), sve.getRoles());
                authenticatedObject.setAuthenticated(true);
                return authenticatedObject;
        }

        @Override
        public boolean supports(Class<?> authentication) {
                return CustomSessionAuthenticationObject.class.equals(authentication);
        }
}
