package com.julien.paymybuddy.exception;

public class ConnectionAlreadyExistException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Connexion already exist";

    public ConnectionAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }
}
