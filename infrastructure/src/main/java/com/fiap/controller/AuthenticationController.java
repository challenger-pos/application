package com.fiap.controller;

import com.fiap.core.domain.auth.LoginRequest;
import com.fiap.core.domain.auth.Token;
import com.fiap.core.exception.InvalidCredentialsException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.auth.LoginUseCase;
import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final LoginUseCase loginUseCase;

    public AuthenticationController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody @Valid LoginRequest loginRequest) throws InvalidCredentialsException, NotFoundException {
        Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("operation.type", "login");
            if (loginRequest.email() != null) {
                span.setTag("user.email", loginRequest.email());
            }
        }
        try {
            // Add context to MDC for structured logging
            MDC.put("operation.type", "login");
            if (loginRequest.email() != null) {
                MDC.put("user.email", loginRequest.email());
            }

            logger.info("Login attempt initiated for email: {}", loginRequest.email());

            Token token = loginUseCase.execute(loginRequest);

            logger.info("Login successful for email: {}", loginRequest.email());

            return ResponseEntity.ok(token);
        } catch (InvalidCredentialsException e) {
            logger.warn("Login failed - invalid credentials for email: {}", loginRequest.email());
            throw e;
        } catch (NotFoundException e) {
            logger.warn("Login failed - user not found for email: {}", loginRequest.email());
            throw e;
        } finally {
            // Clean up MDC
            MDC.remove("operation.type");
            MDC.remove("user.email");
        }
    }
}