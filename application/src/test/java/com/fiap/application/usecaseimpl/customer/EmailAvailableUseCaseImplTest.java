package com.fiap.application.usecaseimpl.customer;

import com.fiap.application.gateway.customer.EmailAvailableGateway;
import com.fiap.usecase.customer.EmailAvailableUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailAvailableUseCaseImplTest {

    @Mock
    EmailAvailableGateway gateway;

    @Test
    void shouldReturnTrueWhenGatewaySaysAvailable() {
        String email = "a@b.com";
        when(gateway.emailAvailable(email)).thenReturn(true);

        EmailAvailableUseCase useCase = new EmailAvailableUseCaseImpl(gateway);
        boolean result = useCase.emailAvailable(email);

        assertTrue(result);
        InOrder inOrder = inOrder(gateway);
        inOrder.verify(gateway).emailAvailable(email);
        verifyNoMoreInteractions(gateway);
    }

    @Test
    void shouldReturnFalseWhenGatewaySaysUnavailable() {
        String email = "a@b.com";
        when(gateway.emailAvailable(email)).thenReturn(false);

        EmailAvailableUseCase useCase = new EmailAvailableUseCaseImpl(gateway);
        boolean result = useCase.emailAvailable(email);

        assertFalse(result);
        InOrder inOrder = inOrder(gateway);
        inOrder.verify(gateway).emailAvailable(email);
        verifyNoMoreInteractions(gateway);
    }
}
