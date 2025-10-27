package com.fiap.controller;

import com.fiap.core.domain.auth.LoginRequest;
import com.fiap.core.domain.auth.Token;
import com.fiap.core.exception.InvalidCredentialsException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.auth.LoginUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private final LoginUseCase loginUseCase;

    public AuthenticationController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody @Valid LoginRequest loginRequest) throws InvalidCredentialsException, NotFoundException {
        Token token = loginUseCase.execute(loginRequest);
        return ResponseEntity.ok(token);
    }
}