package com.fiap.application.usecaseimpl.user;

import com.fiap.application.gateway.user.UserGateway;
import com.fiap.core.domain.user.User;
import com.fiap.core.exception.InternalServerErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {

    @Mock
    UserGateway userGateway;

    @Mock
    User inputUser;

    @Mock
    User createdUser;

    @Test
    void shouldReturnCreatedUserWhenGatewaySucceeds() throws InternalServerErrorException {
        when(userGateway.create(inputUser)).thenReturn(createdUser);

        CreateUserUseCaseImpl useCase = new CreateUserUseCaseImpl(userGateway);
        User result = useCase.execute(inputUser);

        assertSame(createdUser, result);

        InOrder inOrder = inOrder(userGateway);
        inOrder.verify(userGateway).create(inputUser);
        verifyNoMoreInteractions(userGateway);
    }

    @Test
    void shouldThrowInternalServerErrorWhenGatewayReturnsNull() {
        when(userGateway.create(inputUser)).thenReturn(null);

        CreateUserUseCaseImpl useCase = new CreateUserUseCaseImpl(userGateway);

        assertThrows(InternalServerErrorException.class, () -> useCase.execute(inputUser));

        verify(userGateway).create(inputUser);
        verifyNoMoreInteractions(userGateway);
    }
}
