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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByEmailUseCaseImplTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private User user;

    @Test
    void shouldReturnUserWhenFound() throws NotFoundException {
        String email = "a@b.com";
        when(userGateway.findByEmail(email)).thenReturn(Optional.of(user));

        FindUserByEmailUseCaseImpl useCase = new FindUserByEmailUseCaseImpl(userGateway);
        User result = useCase.execute(email);

        assertSame(user, result);

        InOrder inOrder = inOrder(userGateway);
        inOrder.verify(userGateway).findByEmail(email);
        verifyNoMoreInteractions(userGateway);
    }

    @Test
    void shouldThrowNotFoundWhenUserDoesNotExist() throws Exception {
        String email = "missing@b.com";
        when(userGateway.findByEmail(email)).thenReturn(Optional.empty());

        FindUserByEmailUseCaseImpl useCase = new FindUserByEmailUseCaseImpl(userGateway);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> useCase.execute(email));

        assert thrown.getCode().equals(ErrorCodeEnum.USE0007.getCode());
        assert thrown.getMessage().equals(ErrorCodeEnum.USE0007.getMessage());

        verify(userGateway).findByEmail(email);
        verifyNoMoreInteractions(userGateway);
    }
}
