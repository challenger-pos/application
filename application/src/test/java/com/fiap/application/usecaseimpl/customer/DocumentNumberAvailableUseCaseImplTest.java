package com.fiap.application.usecaseimpl.customer;

import com.fiap.application.gateway.customer.DocumentNumberAvailableGateway;
import com.fiap.usecase.customer.DocumentNumberAvailableUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentNumberAvailableUseCaseImplTest {

    @Mock
    DocumentNumberAvailableGateway gateway;

    @Test
    void shouldReturnTrueWhenGatewaySaysAvailable() {
        String doc = "12345678901";
        when(gateway.documentNumberAvailable(doc)).thenReturn(true);

        DocumentNumberAvailableUseCase useCase = new DocumentNumberAvailableUseCaseImpl(gateway);
        boolean result = useCase.documentNumberAvailable(doc);

        assertTrue(result);
        InOrder inOrder = inOrder(gateway);
        inOrder.verify(gateway).documentNumberAvailable(doc);
        verifyNoMoreInteractions(gateway);
    }

    @Test
    void shouldReturnFalseWhenGatewaySaysUnavailable() {
        String doc = "12345678901";
        when(gateway.documentNumberAvailable(doc)).thenReturn(false);

        DocumentNumberAvailableUseCase useCase = new DocumentNumberAvailableUseCaseImpl(gateway);
        boolean result = useCase.documentNumberAvailable(doc);

        assertFalse(result);
        InOrder inOrder = inOrder(gateway);
        inOrder.verify(gateway).documentNumberAvailable(doc);
        verifyNoMoreInteractions(gateway);
    }
}
