package com.implementation.PollingApp.exception.custom;

public class UnauthorizedRequestException extends RuntimeException {

        public UnauthorizedRequestException(String message) {
                super(message);
        }

}
