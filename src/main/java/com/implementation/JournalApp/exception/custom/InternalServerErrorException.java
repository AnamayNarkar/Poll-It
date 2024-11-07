package com.implementation.JournalApp.exception.custom;

public class InternalServerErrorException extends RuntimeException {

        public InternalServerErrorException(String message) {
                super(message);
        }
}
