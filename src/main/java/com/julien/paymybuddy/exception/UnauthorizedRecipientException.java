package com.julien.paymybuddy.exception;

public class UnauthorizedRecipientException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "The specified recipient is not a contact of the sender.";

    public UnauthorizedRecipientException() {
        super(DEFAULT_MESSAGE);
    }
}
