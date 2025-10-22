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
class DeleteCustomerUseCaseImplTest {

    @Mock CustomerGateway customerGateway;
    @Mock Customer customer;

    @Test
    void shouldDeleteWhenCustomerExists() throws Exception {
        UUID id = UUID.randomUUID();
        when(customerGateway.findById(id)).thenReturn(Optional.of(customer));

        DeleteCustomerUseCaseImpl useCase = new DeleteCustomerUseCaseImpl(customerGateway);
        useCase.execute(id);

        InOrder inOrder = inOrder(customerGateway);
        inOrder.verify(customerGateway).findById(id);
        inOrder.verify(customerGateway).delete(customer);
        verifyNoMoreInteractions(customerGateway);
    }

    @Test
    void shouldThrowNotFoundWhenCustomerDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(customerGateway.findById(id)).thenReturn(Optional.empty());

        DeleteCustomerUseCaseImpl useCase = new DeleteCustomerUseCaseImpl(customerGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));

        verify(customerGateway).findById(id);
        verifyNoMoreInteractions(customerGateway);
    }
}
