package com.fiap.core.exception;

public class BusinessRuleException extends DomainException {

    public BusinessRuleException(String message, String code) {
        super(message, code);
    }
}