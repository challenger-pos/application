package com.fiap.core.exception;

public class EmailException extends DomainException {

    public EmailException(String message, String code) {
        super(message, code);
    }
}
