package com.implementation.PollingApp.exception.custom;

public class SessionNotFoundException extends RuntimeException {

        public SessionNotFoundException(String message) {
                super(message);
        }
}
