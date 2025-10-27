package com.fiap.application.usecaseimpl.customer;

import com.fiap.application.gateway.customer.CustomerGateway;
import com.fiap.core.domain.Email;
import com.fiap.core.domain.customer.Customer;
import com.fiap.core.domain.customer.DocumentNumber;
import com.fiap.core.exception.DocumentNumberException;
import com.fiap.core.exception.EmailException;
import com.fiap.core.exception.InternalServerErrorException;
import com.fiap.usecase.customer.DocumentNumberAvailableUseCase;
import com.fiap.usecase.customer.EmailAvailableUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseImplTest {

    @Mock DocumentNumberAvailableUseCase documentNumberAvailableUseCase;
    @Mock EmailAvailableUseCase emailAvailableUseCase;
    @Mock CustomerGateway customerGateway;

    private Customer newCustomer() {
        Customer c = new Customer();
        c.setDocumentNumber(DocumentNumber.fromPersistence("12345678901"));
        c.setEmail(new Email("a@b.com"));
        c.setName("Ana");
        c.setPhone("999");
        return c;
    }

    @Test
    void shouldCreateWhenDocAndEmailAreAvailable() throws DocumentNumberException, EmailException, InternalServerErrorException {
        Customer input = newCustomer();
        Customer created = newCustomer();

        when(documentNumberAvailableUseCase.documentNumberAvailable("12345678901")).thenReturn(true);
        when(emailAvailableUseCase.emailAvailable("a@b.com")).thenReturn(true);
        when(customerGateway.create(input)).thenReturn(created);

        CreateCustomerUseCaseImpl useCase =
                new CreateCustomerUseCaseImpl(documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);

        Customer result = useCase.execute(input);
        assertSame(created, result);

        InOrder order = inOrder(documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);
        order.verify(documentNumberAvailableUseCase).documentNumberAvailable("12345678901");
        order.verify(emailAvailableUseCase).emailAvailable("a@b.com");
        order.verify(customerGateway).create(input);
        verifyNoMoreInteractions(documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);
    }

    @Test
    void shouldThrowWhenDocumentNotAvailable() {
        Customer input = newCustomer();

        when(documentNumberAvailableUseCase.documentNumberAvailable("12345678901")).thenReturn(false);

        CreateCustomerUseCaseImpl useCase =
                new CreateCustomerUseCaseImpl(documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);

        assertThrows(DocumentNumberException.class, () -> useCase.execute(input));

        verify(documentNumberAvailableUseCase).documentNumberAvailable("12345678901");
        verifyNoInteractions(emailAvailableUseCase, customerGateway);
    }

    @Test
    void shouldThrowWhenEmailNotAvailable() throws DocumentNumberException {
        Customer input = newCustomer();

        when(documentNumberAvailableUseCase.documentNumberAvailable("12345678901")).thenReturn(true);
        when(emailAvailableUseCase.emailAvailable("a@b.com")).thenReturn(false);

        CreateCustomerUseCaseImpl useCase =
                new CreateCustomerUseCaseImpl(documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);

        assertThrows(EmailException.class, () -> useCase.execute(input));

        verify(documentNumberAvailableUseCase).documentNumberAvailable("12345678901");
        verify(emailAvailableUseCase).emailAvailable("a@b.com");
        verifyNoInteractions(customerGateway);
    }

    @Test
    void shouldThrowWhenGatewayReturnsNull() throws DocumentNumberException, EmailException {
        Customer input = newCustomer();

        when(documentNumberAvailableUseCase.documentNumberAvailable("12345678901")).thenReturn(true);
        when(emailAvailableUseCase.emailAvailable("a@b.com")).thenReturn(true);
        when(customerGateway.create(input)).thenReturn(null);

        CreateCustomerUseCaseImpl useCase =
                new CreateCustomerUseCaseImpl(documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);

        assertThrows(InternalServerErrorException.class, () -> useCase.execute(input));

        verify(documentNumberAvailableUseCase).documentNumberAvailable("12345678901");
        verify(emailAvailableUseCase).emailAvailable("a@b.com");
        verify(customerGateway).create(input);
        verifyNoMoreInteractions(documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);
    }
}
