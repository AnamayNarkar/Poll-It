package com.implementation.PollingApp.exception.custom;

public class CannotVoteException extends RuntimeException {

        public CannotVoteException(String message) {
                super(message);
        }

}
