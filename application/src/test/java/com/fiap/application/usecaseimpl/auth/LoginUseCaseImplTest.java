package com.fiap.application.usecaseimpl.auth;

import com.fiap.application.gateway.auth.AuthGateway;
import com.fiap.core.domain.auth.LoginRequest;
import com.fiap.core.domain.auth.Token;
import com.fiap.core.domain.user.User;
import com.fiap.core.exception.InvalidCredentialsException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.user.FindUserByEmailUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    AuthGateway authGateway;

    @Mock
    FindUserByEmailUseCase findUserByEmailUseCase;

    @Mock
    User user;

    @Mock
    Token token;

    @Test
    void shouldAuthenticateAndReturnToken() throws NotFoundException, InvalidCredentialsException {
        LoginUseCaseImpl useCase = new LoginUseCaseImpl(authGateway, findUserByEmailUseCase);
        LoginRequest req = new LoginRequest("a@b.com", "secret");

        when(findUserByEmailUseCase.execute("a@b.com")).thenReturn(user);
        when(authGateway.authenticate(user, "secret")).thenReturn(token);

        Token result = useCase.execute(req);

        assertSame(token, result);

        InOrder inOrder = inOrder(findUserByEmailUseCase, authGateway);
        inOrder.verify(findUserByEmailUseCase).execute("a@b.com");
        inOrder.verify(authGateway).authenticate(user, "secret");
        verifyNoMoreInteractions(findUserByEmailUseCase, authGateway);
    }

    @Test
    void shouldPropagateNotFoundWhenUserDoesNotExist() throws NotFoundException {
        LoginUseCaseImpl useCase = new LoginUseCaseImpl(authGateway, findUserByEmailUseCase);
        LoginRequest req = new LoginRequest("missing@b.com", "secret");

        when(findUserByEmailUseCase.execute("missing@b.com")).thenThrow(new NotFoundException("x","y"));

        assertThrows(NotFoundException.class, () -> useCase.execute(req));

        verify(findUserByEmailUseCase).execute("missing@b.com");
        verifyNoInteractions(authGateway);
    }

    @Test
    void shouldPropagateInvalidCredentialsFromGateway() throws NotFoundException, InvalidCredentialsException {
        LoginUseCaseImpl useCase = new LoginUseCaseImpl(authGateway, findUserByEmailUseCase);
        LoginRequest req = new LoginRequest("a@b.com", "wrong");

        when(findUserByEmailUseCase.execute("a@b.com")).thenReturn(user);
        when(authGateway.authenticate(user, "wrong"))
                .thenThrow(new InvalidCredentialsException("invalid credentials"));

        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(req));

        verify(findUserByEmailUseCase).execute("a@b.com");
        verify(authGateway).authenticate(user, "wrong");
        verifyNoMoreInteractions(findUserByEmailUseCase, authGateway);
    }

}
