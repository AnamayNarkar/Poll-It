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

                SessionValueEntity sve = sessionUtils.getUserSession(ca.getRequest());

                if (sve == null) {
                        CustomSessionAuthenticationObject failedAuth = new CustomSessionAuthenticationObject(null, null, null, null);
                        failedAuth.setAuthenticated(false);
                        return failedAuth;
                }

                System.out.println(sve + "\n");

                CustomSessionAuthenticationObject authenticatedObject = new CustomSessionAuthenticationObject(ca.getRequest(), sve.getId(), sve.getUsername(), sve.getRoles());

                if (authenticatedObject.getRoles().contains("USER")) {
                        authenticatedObject.setAuthenticated(true);
                } else {
                        authenticatedObject.setAuthenticated(false);
                }

                System.out.println(authenticatedObject);

                return authenticatedObject;
        }

        @Override
        public boolean supports(Class<?> authentication) {
                return CustomSessionAuthenticationObject.class.equals(authentication);
        }
}
