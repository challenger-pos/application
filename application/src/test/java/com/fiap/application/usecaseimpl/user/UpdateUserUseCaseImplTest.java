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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseImplTest {

    @Mock
    UserGateway userGateway;

    @Mock
    User inputUser;

    @Mock
    User updatedUser;

    @Test
    void shouldFindThenUpdateAndReturnUpdatedUser() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(inputUser.getId()).thenReturn(id);
        when(userGateway.findById(id)).thenReturn(Optional.of(mock(User.class)));
        when(userGateway.update(inputUser)).thenReturn(updatedUser);

        UpdateUserUseCaseImpl useCase = new UpdateUserUseCaseImpl(userGateway);
        User result = useCase.execute(inputUser);

        assertSame(updatedUser, result);

        InOrder inOrder = inOrder(userGateway);
        inOrder.verify(userGateway).findById(id);
        inOrder.verify(userGateway).update(inputUser);
        verifyNoMoreInteractions(userGateway);
    }

    @Test
    void shouldThrowNotFoundWhenUserDoesNotExist() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(inputUser.getId()).thenReturn(id);

        doReturn(Optional.empty()).when(userGateway).findById(id);

        UpdateUserUseCaseImpl useCase = new UpdateUserUseCaseImpl(userGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(inputUser));

        verify(userGateway).findById(id);
        verifyNoMoreInteractions(userGateway);
    }

}
