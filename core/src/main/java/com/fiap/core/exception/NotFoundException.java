package com.fiap.core.exception;

public class NotFoundException extends DomainException {

    public NotFoundException(String message, String code) {
        super(message, code);
    }
}
