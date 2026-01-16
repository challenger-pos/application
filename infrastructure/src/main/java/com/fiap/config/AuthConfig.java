package com.fiap.config;

import com.fiap.application.gateway.auth.AuthGateway;
import com.fiap.application.gateway.user.UserGateway;
import com.fiap.application.usecaseimpl.auth.LoginUseCaseImpl;
import com.fiap.application.usecaseimpl.user.FindUserByEmailUseCaseImpl;
import com.fiap.gateway.auth.AuthRepositoryGateway;
import com.fiap.security.jwt.TokenService;
import com.fiap.usecase.auth.LoginUseCase;
import com.fiap.usecase.user.FindUserByEmailUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class AuthConfig {

    @Bean
    public FindUserByEmailUseCase findUserByEmailUseCase(UserGateway userGateway) {
        return new FindUserByEmailUseCaseImpl(userGateway);
    }

    @Bean
    public AuthGateway authGateway(AuthenticationManager authenticationManager, TokenService tokenService) {
        return new AuthRepositoryGateway(authenticationManager, tokenService);
    }

    @Bean
    public LoginUseCase loginUseCase(AuthGateway authGateway, FindUserByEmailUseCase findUserByEmailUseCase) {
        return new LoginUseCaseImpl(authGateway, findUserByEmailUseCase);
    }
}