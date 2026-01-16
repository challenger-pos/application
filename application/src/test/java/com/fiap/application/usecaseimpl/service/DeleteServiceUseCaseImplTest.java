package com.fiap.application.usecaseimpl.service;

import com.fiap.application.gateway.service.ServiceGateway;
import com.fiap.core.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteServiceUseCaseImplTest {

    @Mock
    ServiceGateway serviceGateway;

    @Test
    void shouldDeleteServiceWhenExists() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(serviceGateway.existsById(id)).thenReturn(true);

        DeleteServiceUseCaseImpl useCase = new DeleteServiceUseCaseImpl(serviceGateway);
        useCase.execute(id);

        InOrder inOrder = inOrder(serviceGateway);
        inOrder.verify(serviceGateway).existsById(id);
        inOrder.verify(serviceGateway).delete(id);
        verifyNoMoreInteractions(serviceGateway);
    }

    @Test
    void shouldThrowNotFoundWhenServiceDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(serviceGateway.existsById(id)).thenReturn(false);

        DeleteServiceUseCaseImpl useCase = new DeleteServiceUseCaseImpl(serviceGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));

        verify(serviceGateway).existsById(id);
        verifyNoMoreInteractions(serviceGateway);
    }
}
