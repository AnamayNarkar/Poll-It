package com.implementation.JournalApp.exception.custom;

public class ForbiddenRequestException extends RuntimeException {

        public ForbiddenRequestException(String message) {
                super(message);
        }

}
