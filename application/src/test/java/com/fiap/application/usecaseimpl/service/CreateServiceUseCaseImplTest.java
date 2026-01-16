package com.fiap.application.usecaseimpl.service;

import com.fiap.application.gateway.service.ServiceGateway;
import com.fiap.core.domain.service.Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateServiceUseCaseImplTest {

    @Mock
    ServiceGateway serviceGateway;

    @Mock
    Service service;

    @Mock
    Service createdService;

    @Test
    void shouldCreateServiceSuccessfully() {
        when(serviceGateway.create(service)).thenReturn(createdService);

        CreateServiceUseCaseImpl useCase = new CreateServiceUseCaseImpl(serviceGateway);
        Service result = useCase.execute(service);

        assertSame(createdService, result);

        InOrder inOrder = inOrder(serviceGateway);
        inOrder.verify(serviceGateway).create(service);
        verifyNoMoreInteractions(serviceGateway);
    }
}
