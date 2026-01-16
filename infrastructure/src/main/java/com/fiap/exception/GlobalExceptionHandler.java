package com.fiap.exception;

import com.fiap.core.exception.*;
import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final Map<Class<? extends DomainException>, HttpStatus> exceptionStatusMap = new HashMap<>();

    public GlobalExceptionHandler() {
        exceptionStatusMap.put(BadRequestException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(DocumentNumberException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(EmailException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(PasswordException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(NotFoundException.class, HttpStatus.NOT_FOUND);
        exceptionStatusMap.put(InternalServerErrorException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionStatusMap.put(BusinessRuleException.class, HttpStatus.CONFLICT);
        exceptionStatusMap.put(UnauthorizedException.class, HttpStatus.UNAUTHORIZED);
        exceptionStatusMap.put(ForbiddenException.class, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        addErrorToSpan(ex);
        HttpStatus status = exceptionStatusMap.getOrDefault(ex.getClass(), HttpStatus.BAD_REQUEST);

        // Add error context to MDC for structured logging
        MDC.put("error", "true");
        MDC.put("error.type", ex.getClass().getSimpleName());
        MDC.put("error.code", ex.getCode() != null ? ex.getCode() : "DOMAIN_ERROR");
        MDC.put("error.message", ex.getMessage() != null ? ex.getMessage() : "Unknown error");
        MDC.put("http.status_code", String.valueOf(status.value()));

        logger.error("Domain exception occurred: {} - Code: {} - Message: {}",
                ex.getClass().getSimpleName(), ex.getCode(), ex.getMessage(), ex);

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                ex.getCode() != null ? ex.getCode() : "DOMAIN_ERROR",
                ex.getMessage()
        );

        // Clean up MDC
        MDC.remove("error");
        MDC.remove("error.type");
        MDC.remove("error.code");
        MDC.remove("error.message");
        MDC.remove("http.status_code");

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        addErrorToSpan(ex);

        // Add error context to MDC for structured logging
        MDC.put("error", "true");
        MDC.put("error.type", ex.getClass().getName());
        MDC.put("error.message", ex.getMessage() != null ? ex.getMessage() : "Unknown error");
        MDC.put("http.status_code", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));

        logger.error("Unexpected exception occurred: {} - Message: {}",
                ex.getClass().getName(), ex.getMessage(), ex);

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                "INTERNAL_ERROR",
                ex.getMessage()
        );

        // Clean up MDC
        MDC.remove("error");
        MDC.remove("error.type");
        MDC.remove("error.message");
        MDC.remove("http.status_code");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private void addErrorToSpan(Exception ex) {
        Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("error",true);
            span.setTag("error.type", ex.getClass().getName());
            span.setTag("error.message", ex.getMessage() != null ? ex.getMessage() : "Unknown error");
            if (ex instanceof DomainException domainEx && domainEx.getCode() != null) {
                span.setTag("error.code", domainEx.getCode());
            }
            span.setTag("error.stack", getStackTrace(ex));
        }
    }

    private String getStackTrace(Exception ex) {
        var sw = new java.io.StringWriter();
        var pw = new java.io.PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
