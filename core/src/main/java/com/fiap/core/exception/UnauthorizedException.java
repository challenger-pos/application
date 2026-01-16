package com.fiap.core.exception;

public class UnauthorizedException extends DomainException {
    public UnauthorizedException(String message, String code) {
        super(message, code);
    }
}
