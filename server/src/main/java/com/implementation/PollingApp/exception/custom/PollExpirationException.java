package com.implementation.PollingApp.exception.custom;

public class PollExpirationException extends RuntimeException {

        public PollExpirationException(String message) {
                super(message);
        }

}
