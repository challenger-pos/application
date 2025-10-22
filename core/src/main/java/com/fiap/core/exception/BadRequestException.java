package com.fiap.core.exception;

public class BadRequestException extends DomainException {

    public BadRequestException(String message, String code) {
        super(message, code);
    }
}
