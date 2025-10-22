package com.fiap.application.usecaseimpl.user;

import com.fiap.application.gateway.user.UserGateway;
import com.fiap.core.domain.user.User;
import com.fiap.core.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByIdUseCaseImplTest {

    @Mock
    UserGateway userGateway;

    @Mock
    User user;

    @Test
    void shouldReturnUserWhenFound() throws NotFoundException {
        UUID id = UUID.randomUUID();
        Optional<User> expected = Optional.of(user);
        when(userGateway.findById(id)).thenReturn(expected);

        FindUserByIdUseCaseImpl useCase = new FindUserByIdUseCaseImpl(userGateway);
        Optional<User> result = useCase.execute(id);

        assertSame(expected, result);

        InOrder inOrder = inOrder(userGateway);
        inOrder.verify(userGateway).findById(id);
        verifyNoMoreInteractions(userGateway);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(userGateway.findById(id)).thenReturn(Optional.empty());

        FindUserByIdUseCaseImpl useCase = new FindUserByIdUseCaseImpl(userGateway);
        Optional<User> result = useCase.execute(id);

        assert result.isEmpty();

        verify(userGateway).findById(id);
        verifyNoMoreInteractions(userGateway);
    }
}
