package com.fiap.gateway.auth;

import com.fiap.application.gateway.auth.AuthGateway;
import com.fiap.core.domain.auth.Token;
import com.fiap.core.domain.user.User;
import com.fiap.core.exception.InvalidCredentialsException;
import com.fiap.security.jwt.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthRepositoryGateway implements AuthGateway {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthRepositoryGateway(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public Token authenticate(User user, String password) throws InvalidCredentialsException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), password));
            String token = tokenService.generateToken(user);
            return new Token(token);
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }
}