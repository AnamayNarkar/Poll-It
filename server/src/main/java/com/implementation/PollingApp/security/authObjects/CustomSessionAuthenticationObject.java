package com.implementation.PollingApp.security.authObjects;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.security.auth.Subject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.implementation.PollingApp.entity.SessionValueEntity;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomSessionAuthenticationObject implements Authentication {

    private boolean authentication;

    private HttpServletRequest request;

    private String sessionId;

    private SessionValueEntity sessionValueEntity;

    // constructor
    public CustomSessionAuthenticationObject(HttpServletRequest request) {
        this.request = request;
        this.sessionId = null;
        this.sessionValueEntity = null;
    }

    public CustomSessionAuthenticationObject(HttpServletRequest request, String sessionId, SessionValueEntity sessionValueEntity) {
        this.request = request;
        this.sessionId = sessionId;
        this.sessionValueEntity = sessionValueEntity;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    public boolean isAuthenticated() {
        return authentication;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authentication = isAuthenticated;
    }

    // not needed for now
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return Authentication.super.implies(subject);
    }

    @Override
    public String getName() {
        return null;
    }
}