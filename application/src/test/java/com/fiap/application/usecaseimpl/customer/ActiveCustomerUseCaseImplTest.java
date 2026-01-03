package com.fiap.application.usecaseimpl.customer;

import com.fiap.application.gateway.customer.CustomerGateway;
import com.fiap.core.domain.customer.Customer;
import com.fiap.core.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActiveCustomerUseCaseImplTest {

    @Mock
    CustomerGateway customerGateway;

    @Mock
    Customer customer;

    @Test
    void shouldActivateCustomerWhenCustomerIsInactive() throws Exception {
        UUID id = UUID.randomUUID();

        when(customerGateway.findById(id)).thenReturn(Optional.of(customer));
        when(customer.getIsActive()).thenReturn(false);

        ActiveCustomerUseCaseImpl useCase =
                new ActiveCustomerUseCaseImpl(customerGateway);

        useCase.execute(id);

        InOrder inOrder = inOrder(customerGateway, customer);
        inOrder.verify(customerGateway).findById(id);
        inOrder.verify(customer).setIsActive(true);
        inOrder.verify(customerGateway).update(customer);

        verifyNoMoreInteractions(customerGateway, customer);
    }

    @Test
    void shouldNotUpdateWhenCustomerIsAlreadyActive() throws Exception {
        UUID id = UUID.randomUUID();

        when(customerGateway.findById(id)).thenReturn(Optional.of(customer));
        when(customer.getIsActive()).thenReturn(true);

        ActiveCustomerUseCaseImpl useCase =
                new ActiveCustomerUseCaseImpl(customerGateway);

        useCase.execute(id);

        verify(customerGateway).findById(id);
        verify(customer).getIsActive();
        verifyNoMoreInteractions(customerGateway, customer);
    }

    @Test
    void shouldThrowNotFoundWhenCustomerDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(customerGateway.findById(id)).thenReturn(Optional.empty());

        ActiveCustomerUseCaseImpl useCase =
                new ActiveCustomerUseCaseImpl(customerGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));

        verify(customerGateway).findById(id);
        verifyNoMoreInteractions(customerGateway);
    }
}
