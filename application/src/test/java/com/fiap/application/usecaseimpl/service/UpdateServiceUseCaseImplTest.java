package com.fiap.application.usecaseimpl.service;

import com.fiap.application.gateway.service.ServiceGateway;
import com.fiap.core.domain.service.Service;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.service.FindServiceByIdUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateServiceUseCaseImplTest {

    @Mock
    ServiceGateway serviceGateway;

    @Mock
    FindServiceByIdUseCase findServiceByIdUseCase;

    @Mock
    Service existingService;

    @Mock
    Service service;

    @Mock
    Service updatedService;

    @Test
    void shouldUpdateServiceWhenFound() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(service.getId()).thenReturn(id);
        when(findServiceByIdUseCase.execute(id)).thenReturn(existingService);
        when(serviceGateway.update(existingService)).thenReturn(updatedService);

        UpdateServiceUseCaseImpl useCase = new UpdateServiceUseCaseImpl(serviceGateway, findServiceByIdUseCase);
        Service result = useCase.execute(service);

        assertSame(updatedService, result);

        InOrder inOrder = inOrder(findServiceByIdUseCase, serviceGateway);
        inOrder.verify(findServiceByIdUseCase).execute(id);
        inOrder.verify(serviceGateway).update(existingService);
        verify(existingService).setName(service.getName());
        verify(existingService).setDescription(service.getDescription());
        verify(existingService).setBasePrice(service.getBasePrice());
        verify(existingService).setEstimatedTimeMin(service.getEstimatedTimeMin());
        verifyNoMoreInteractions(findServiceByIdUseCase, serviceGateway, existingService);
    }

    @Test
    void shouldThrowNotFoundWhenServiceDoesNotExist() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(service.getId()).thenReturn(id);
        when(findServiceByIdUseCase.execute(id)).thenThrow(new NotFoundException("Service not found", "SERVICE-404"));

        UpdateServiceUseCaseImpl useCase = new UpdateServiceUseCaseImpl(serviceGateway, findServiceByIdUseCase);

        assertThrows(NotFoundException.class, () -> useCase.execute(service));

        verify(findServiceByIdUseCase).execute(id);
        verifyNoInteractions(serviceGateway);
    }
}
