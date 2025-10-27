package com.fiap.application.usecaseimpl.auth;

import com.fiap.application.gateway.auth.AuthGateway;
import com.fiap.core.domain.auth.LoginRequest;
import com.fiap.core.domain.auth.Token;
import com.fiap.core.domain.user.User;
import com.fiap.core.exception.InvalidCredentialsException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.auth.LoginUseCase;
import com.fiap.usecase.user.FindUserByEmailUseCase;

public class LoginUseCaseImpl implements LoginUseCase {

    private final AuthGateway authGateway;
    private final FindUserByEmailUseCase findUserByEmailUseCase;

    public LoginUseCaseImpl(AuthGateway authGateway, FindUserByEmailUseCase findUserByEmailUseCase) {
        this.authGateway = authGateway;
        this.findUserByEmailUseCase = findUserByEmailUseCase;
    }

    @Override
    public Token execute(LoginRequest loginRequest) throws InvalidCredentialsException, NotFoundException {
        User user = findUserByEmailUseCase.execute(loginRequest.email());
        return authGateway.authenticate(user, loginRequest.password());
    }
}