package com.fiap.application.usecaseimpl.service;

import com.fiap.application.gateway.service.ServiceGateway;
import com.fiap.core.domain.service.Service;
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
class FindServiceByIdUseCaseImplTest {

    @Mock
    ServiceGateway serviceGateway;

    @Mock
    Service service;

    @Test
    void shouldReturnServiceWhenFound() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(serviceGateway.findById(id)).thenReturn(Optional.of(service));

        FindServiceByIdUseCaseImpl useCase = new FindServiceByIdUseCaseImpl(serviceGateway);
        Service result = useCase.execute(id);

        assertSame(service, result);

        InOrder inOrder = inOrder(serviceGateway);
        inOrder.verify(serviceGateway).findById(id);
        verifyNoMoreInteractions(serviceGateway);
    }

    @Test
    void shouldThrowNotFoundWhenServiceDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(serviceGateway.findById(id)).thenReturn(Optional.empty());

        FindServiceByIdUseCaseImpl useCase = new FindServiceByIdUseCaseImpl(serviceGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));

        verify(serviceGateway).findById(id);
        verifyNoMoreInteractions(serviceGateway);
    }
}
