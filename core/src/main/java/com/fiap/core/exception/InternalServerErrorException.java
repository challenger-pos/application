package com.fiap.core.exception;

public class InternalServerErrorException extends DomainException {

    public InternalServerErrorException(String message, String code) {
        super(message, code);
    }
}
