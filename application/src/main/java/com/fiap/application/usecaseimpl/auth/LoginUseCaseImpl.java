package com.fiap.application.usecaseimpl.auth;

import com.fiap.application.gateway.auth.AuthGateway;
import com.fiap.core.domain.auth.LoginRequest;
import com.fiap.core.domain.auth.Token;
import com.fiap.core.domain.user.User;
import com.fiap.core.exception.InvalidCredentialsException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.auth.LoginUseCase;
import com.fiap.usecase.user.FindUserByEmailUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LoginUseCaseImpl implements LoginUseCase {

    private static final Logger logger = LoggerFactory.getLogger(LoginUseCaseImpl.class);
    private final AuthGateway authGateway;
    private final FindUserByEmailUseCase findUserByEmailUseCase;

    public LoginUseCaseImpl(AuthGateway authGateway, FindUserByEmailUseCase findUserByEmailUseCase) {
        this.authGateway = authGateway;
        this.findUserByEmailUseCase = findUserByEmailUseCase;
    }

  @Override
  public Token execute(LoginRequest loginRequest)
      throws InvalidCredentialsException, NotFoundException {
        try {
          logger.debug("Executing login use case for email: {}", loginRequest.email());

          User user = findUserByEmailUseCase.execute(loginRequest.email());

          if (user != null && user.getId() != null) {
            MDC.put("user.id", user.getId().toString());
            logger.debug("User found for email: {} - User ID: {}", loginRequest.email(), user.getId());
          }

          Token token = authGateway.authenticate(user, loginRequest.password());

          logger.info("Authentication successful for email: {}", loginRequest.email());

          return token;
        } catch (NotFoundException e) {
          logger.warn("User not found for email: {}", loginRequest.email());
          throw e;
        } catch (InvalidCredentialsException e) {
          logger.warn("Invalid credentials for email: {}", loginRequest.email());
          throw e;
        } finally {
          MDC.remove("user.id");
        }
    }
}