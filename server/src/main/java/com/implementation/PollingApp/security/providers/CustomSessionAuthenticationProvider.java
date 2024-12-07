package com.implementation.PollingApp.security.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.implementation.PollingApp.entity.SessionValueEntity;
import com.implementation.PollingApp.security.authObjects.CustomSessionAuthenticationObject;
import com.implementation.PollingApp.util.SessionUtils;

@Component
public class CustomSessionAuthenticationProvider implements AuthenticationProvider {

        @Autowired
        private SessionUtils sessionUtils;

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                CustomSessionAuthenticationObject ca = (CustomSessionAuthenticationObject) authentication;

                SessionValueEntity sve = sessionUtils.getUserSession(ca.getRequest());

                if (sve == null) {
                        CustomSessionAuthenticationObject failedAuth = new CustomSessionAuthenticationObject(null, null, null);
                        failedAuth.setAuthenticated(false);
                        return failedAuth;
                }

                String sessionId = sessionUtils.getSessionIdFromRequest(ca.getRequest());

                CustomSessionAuthenticationObject authenticatedObject = new CustomSessionAuthenticationObject(ca.getRequest(), sessionId, sve);

                // if (authenticatedObject.getRoles().contains("USER")) {

                if (authenticatedObject.getSessionValueEntity().getRoles().contains("USER")) {
                        authenticatedObject.setAuthenticated(true);
                } else {
                        authenticatedObject.setAuthenticated(false);
                }

                return authenticatedObject;
        }

        @Override
        public boolean supports(Class<?> authentication) {
                return CustomSessionAuthenticationObject.class.equals(authentication);
        }
}
