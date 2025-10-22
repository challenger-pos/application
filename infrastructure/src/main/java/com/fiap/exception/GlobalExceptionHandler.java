package com.fiap.exception;

import com.fiap.core.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Map<Class<? extends DomainException>, HttpStatus> exceptionStatusMap = new HashMap<>();

    public GlobalExceptionHandler() {
        exceptionStatusMap.put(BadRequestException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(DocumentNumberException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(EmailException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(PasswordException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(NotFoundException.class, HttpStatus.NOT_FOUND);
        exceptionStatusMap.put(InternalServerErrorException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionStatusMap.put(BusinessRuleException.class, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        HttpStatus status = exceptionStatusMap.getOrDefault(ex.getClass(), HttpStatus.BAD_REQUEST);

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                ex.getCode() != null ? ex.getCode() : "DOMAIN_ERROR",
                ex.getMessage()
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                "INTERNAL_ERROR",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
