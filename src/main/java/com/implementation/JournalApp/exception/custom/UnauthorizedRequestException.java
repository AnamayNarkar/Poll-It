package com.implementation.JournalApp.exception.custom;

public class UnauthorizedRequestException extends RuntimeException {

        public UnauthorizedRequestException(String message) {
                super(message);
        }

}
