package com.implementation.JournalApp.exception.custom;

public class ResourceNotFoundException extends RuntimeException {

        public ResourceNotFoundException(String message) {
                super(message);
        }
}
