package com.fiap.application.usecaseimpl.customer;

import com.fiap.application.gateway.customer.CustomerGateway;
import com.fiap.core.domain.customer.Customer;
import com.fiap.core.exception.DocumentNumberException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.customer.FindCustomerByDocumentUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindCustomerByDocumentUseCaseImplTest {

    @Mock
    CustomerGateway customerGateway;

    @Mock
    Customer customer;

    @Test
    void shouldReturnCustomerWhenFound() throws DocumentNumberException, NotFoundException {
        String doc = "12345678901";
        when(customerGateway.findByDocumentNumber(doc)).thenReturn(Optional.of(customer));

        FindCustomerByDocumentUseCase useCase = new FindCustomerByDocumentUseCaseImpl(customerGateway);
        Customer result = useCase.execute(doc);

        assertSame(customer, result);
        InOrder inOrder = inOrder(customerGateway);
        inOrder.verify(customerGateway).findByDocumentNumber(doc);
        verifyNoMoreInteractions(customerGateway);
    }

    @Test
    void shouldThrowNotFoundWhenMissing() throws DocumentNumberException {
        String doc = "12345678901";
        when(customerGateway.findByDocumentNumber(doc)).thenReturn(Optional.empty());

        FindCustomerByDocumentUseCase useCase = new FindCustomerByDocumentUseCaseImpl(customerGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(doc));
        InOrder inOrder = inOrder(customerGateway);
        inOrder.verify(customerGateway).findByDocumentNumber(doc);
        verifyNoMoreInteractions(customerGateway);
    }

    @Test
    void shouldBubbleUpRuntimeExceptionsFromGateway() throws DocumentNumberException {
        String doc = "invalid";
        when(customerGateway.findByDocumentNumber(doc))
                .thenThrow(new RuntimeException("boom"));

        FindCustomerByDocumentUseCase useCase = new FindCustomerByDocumentUseCaseImpl(customerGateway);

        assertThrows(RuntimeException.class, () -> useCase.execute(doc));

        InOrder inOrder = inOrder(customerGateway);
        inOrder.verify(customerGateway).findByDocumentNumber(doc);
        verifyNoMoreInteractions(customerGateway);
    }

}
