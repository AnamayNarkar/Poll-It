package com.implementation.PollingApp.exception.custom;

public class InternalServerErrorException extends RuntimeException {

        public InternalServerErrorException(String message) {
                super(message);
        }
}
