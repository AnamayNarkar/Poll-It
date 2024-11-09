package com.implementation.JournalApp.security.authObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.security.auth.Subject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

@AllArgsConstructor
@Setter
@Getter
@Data
public class CustomSessionAuthenticationObject implements Authentication {

        private boolean authentication;

        private HttpServletRequest request;

        private String id;

        private String username;

        private Vector<String> roles;

        // constructor
        public CustomSessionAuthenticationObject(HttpServletRequest request) {
                this.request = request;
                this.id = null;
                this.username = null;
                this.roles = null;
        }

        public CustomSessionAuthenticationObject(HttpServletRequest request, String id, String username, Vector<String> roles) {
                this.request = request;
                this.id = id;
                this.username = username;
                this.roles = roles;
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

        public Collection<? extends GrantedAuthority> getAuthorities() {
                return new ArrayList<>(); // Or return null
        }

        // not needed for now

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