package com.implementation.PollingApp.exception.custom;

public class ResourceNotFoundException extends RuntimeException {

        public ResourceNotFoundException(String message) {
                super(message);
        }
}
