package com.fiap.application.usecaseimpl.customer;

import com.fiap.application.gateway.customer.CustomerGateway;
import com.fiap.core.domain.customer.Customer;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.customer.FindCustomerByIdUseCase;
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
class FindCustomerByIdUseCaseImplTest {

    @Mock
    CustomerGateway customerGateway;

    @Test
    void shouldReturnCustomerWhenFound() throws NotFoundException {
        UUID id = UUID.randomUUID();
        Customer c = new Customer(id);
        when(customerGateway.findById(id)).thenReturn(Optional.of(c));

        FindCustomerByIdUseCase useCase = new FindCustomerByIdUseCaseImpl(customerGateway);
        Customer result = useCase.execute(id);

        assertSame(c, result);
        InOrder inOrder = inOrder(customerGateway);
        inOrder.verify(customerGateway).findById(id);
        verifyNoMoreInteractions(customerGateway);
    }

    @Test
    void shouldThrowNotFoundWhenMissing() {
        UUID id = UUID.randomUUID();
        when(customerGateway.findById(id)).thenReturn(Optional.empty());

        FindCustomerByIdUseCase useCase = new FindCustomerByIdUseCaseImpl(customerGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));
        InOrder inOrder = inOrder(customerGateway);
        inOrder.verify(customerGateway).findById(id);
        verifyNoMoreInteractions(customerGateway);
    }
}
