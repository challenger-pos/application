package com.fiap.application.usecaseimpl.service;

import com.fiap.application.gateway.service.ServiceGateway;
import com.fiap.core.domain.service.Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindServicesByIdsUseCaseImplTest {

    @Mock
    ServiceGateway serviceGateway;

    @Mock
    Service s1;

    @Mock
    Service s2;

    @Test
    void shouldReturnServicesFromGateway() {
        List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());
        List<Service> expected = List.of(s1, s2);

        when(serviceGateway.findByIds(ids)).thenReturn(expected);

        FindServicesByIdsUseCaseImpl useCase = new FindServicesByIdsUseCaseImpl(serviceGateway);
        List<Service> result = useCase.execute(ids);

        assertSame(expected, result);

        InOrder inOrder = inOrder(serviceGateway);
        inOrder.verify(serviceGateway).findByIds(ids);
        verifyNoMoreInteractions(serviceGateway);
    }
}
