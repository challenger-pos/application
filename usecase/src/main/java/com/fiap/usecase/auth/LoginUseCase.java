package com.fiap.usecase.auth;

import com.fiap.core.domain.auth.LoginRequest;
import com.fiap.core.domain.auth.Token;
import com.fiap.core.exception.InvalidCredentialsException;
import com.fiap.core.exception.NotFoundException;

public interface LoginUseCase {
    Token execute(LoginRequest loginRequest) throws InvalidCredentialsException, NotFoundException;
}