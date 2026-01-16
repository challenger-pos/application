package com.fiap.application.gateway.auth;

import com.fiap.core.domain.auth.Token;
import com.fiap.core.domain.user.User;
import com.fiap.core.exception.InvalidCredentialsException;

public interface AuthGateway {
    Token authenticate(User user, String password) throws InvalidCredentialsException;
}