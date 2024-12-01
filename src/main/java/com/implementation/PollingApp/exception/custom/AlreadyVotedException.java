package com.implementation.PollingApp.exception.custom;

public class AlreadyVotedException extends RuntimeException {

        public AlreadyVotedException(String message) {
                super(message);
        }

}
