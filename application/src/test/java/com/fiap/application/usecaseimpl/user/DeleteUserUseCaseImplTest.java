package com.fiap.application.usecaseimpl.user;

import com.fiap.application.gateway.user.UserGateway;
import com.fiap.core.domain.user.User;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseImplTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private User user;

    @Test
    void shouldDeleteUserWhenFound() throws NotFoundException {

        UUID id = UUID.randomUUID();
        when(userGateway.findById(id)).thenReturn(Optional.of(user));

        DeleteUserUseCaseImpl useCase = new DeleteUserUseCaseImpl(userGateway);

        useCase.execute(id);

        InOrder inOrder = inOrder(userGateway);
        inOrder.verify(userGateway).findById(id);
        inOrder.verify(userGateway).delete(user);
        verifyNoMoreInteractions(userGateway);
    }

    @Test
    void shouldThrowNotFoundWhenUserDoesNotExist() {
        try {
            UUID id = UUID.randomUUID();
            doReturn(Optional.empty()).when(userGateway).findById(id);

            DeleteUserUseCaseImpl useCase = new DeleteUserUseCaseImpl(userGateway);

            NotFoundException thrown = assertThrows(NotFoundException.class, () -> useCase.execute(id));

            assert thrown.getCode().equals(ErrorCodeEnum.USE0007.getCode());
            assert thrown.getMessage().equals(ErrorCodeEnum.USE0007.getMessage());

            verify(userGateway).findById(id);
            verifyNoMoreInteractions(userGateway);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
