package com.fiap.core.exception;

public class ForbiddenException extends DomainException {
    public ForbiddenException(String message, String code) {
        super(message, code);
    }
}
