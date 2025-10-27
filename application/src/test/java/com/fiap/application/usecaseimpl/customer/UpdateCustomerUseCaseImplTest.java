package com.fiap.application.usecaseimpl.customer;

import com.fiap.application.gateway.customer.CustomerGateway;
import com.fiap.core.domain.customer.Customer;
import com.fiap.core.domain.customer.DocumentNumber;
import com.fiap.core.exception.DocumentNumberException;
import com.fiap.core.exception.EmailException;
import com.fiap.core.exception.InternalServerErrorException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.customer.DocumentNumberAvailableUseCase;
import com.fiap.usecase.customer.EmailAvailableUseCase;
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
class UpdateCustomerUseCaseImplTest {

    @Mock
    DocumentNumberAvailableUseCase documentNumberAvailableUseCase;

    @Mock
    EmailAvailableUseCase emailAvailableUseCase;

    @Mock
    CustomerGateway customerGateway;

    private Customer customer(UUID id, String name, String doc, String phone, String email) throws EmailException, DocumentNumberException {
        return new Customer(id, name, DocumentNumber.of(doc), phone, email);
    }

    @Test
    void shouldUpdateWhenNoChangeOnDocAndEmail() throws Exception {
        UUID id = UUID.randomUUID();
        Customer oldC = customer(id, "A", "39053344705", "9999", "a@b.com");
        Customer updC = customer(id, "A+", "39053344705", "8888", "a@b.com");
        when(customerGateway.findById(id)).thenReturn(Optional.of(oldC));
        when(customerGateway.update(updC)).thenReturn(updC);

        UpdateCustomerUseCaseImpl useCase = new UpdateCustomerUseCaseImpl(
                documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);

        Customer result = useCase.execute(updC);

        assertSame(updC, result);
        InOrder inOrder = inOrder(customerGateway);
        inOrder.verify(customerGateway).findById(id);
        inOrder.verify(customerGateway).update(updC);
        verifyNoMoreInteractions(customerGateway);
        verifyNoInteractions(documentNumberAvailableUseCase, emailAvailableUseCase);
    }

    @Test
    void shouldUpdateWhenDocAndEmailChangedAndAvailable() throws Exception {
        UUID id = UUID.randomUUID();
        Customer oldC = customer(id, "A", "39053344705", "9999", "a@b.com");
        Customer updC = customer(id, "A", "27865757000102", "9999", "x@y.com");
        when(customerGateway.findById(id)).thenReturn(Optional.of(oldC));
        when(documentNumberAvailableUseCase.documentNumberAvailable("27865757000102")).thenReturn(true);
        when(emailAvailableUseCase.emailAvailable("x@y.com")).thenReturn(true);
        when(customerGateway.update(updC)).thenReturn(updC);

        UpdateCustomerUseCaseImpl useCase = new UpdateCustomerUseCaseImpl(
                documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);

        Customer result = useCase.execute(updC);

        assertSame(updC, result);
        InOrder inOrder = inOrder(customerGateway, documentNumberAvailableUseCase, emailAvailableUseCase);
        inOrder.verify(customerGateway).findById(id);
        inOrder.verify(documentNumberAvailableUseCase).documentNumberAvailable("27865757000102");
        inOrder.verify(emailAvailableUseCase).emailAvailable("x@y.com");
        inOrder.verify(customerGateway).update(updC);
        verifyNoMoreInteractions(customerGateway, documentNumberAvailableUseCase, emailAvailableUseCase);
    }

    @Test
    void shouldThrowNotFoundWhenCustomerDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        Customer updC = customer(id, "A", "39053344705", "9999", "a@b.com");
        when(customerGateway.findById(id)).thenReturn(Optional.empty());

        UpdateCustomerUseCaseImpl useCase = new UpdateCustomerUseCaseImpl(
                documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(updC));
        InOrder inOrder = inOrder(customerGateway);
        inOrder.verify(customerGateway).findById(id);
        verifyNoMoreInteractions(customerGateway);
        verifyNoInteractions(documentNumberAvailableUseCase, emailAvailableUseCase);
    }

    @Test
    void shouldThrowDocumentNumberExceptionWhenDocChangedAndUnavailable() throws Exception {
        UUID id = UUID.randomUUID();
        Customer oldC = customer(id, "A", "39053344705", "9999", "a@b.com");
        Customer updC = customer(id, "A", "27865757000102", "9999", "a@b.com");
        when(customerGateway.findById(id)).thenReturn(Optional.of(oldC));
        when(documentNumberAvailableUseCase.documentNumberAvailable("27865757000102")).thenReturn(false);

        UpdateCustomerUseCaseImpl useCase = new UpdateCustomerUseCaseImpl(
                documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);

        assertThrows(DocumentNumberException.class, () -> useCase.execute(updC));
        InOrder inOrder = inOrder(customerGateway, documentNumberAvailableUseCase);
        inOrder.verify(customerGateway).findById(id);
        inOrder.verify(documentNumberAvailableUseCase).documentNumberAvailable("27865757000102");
        verifyNoMoreInteractions(customerGateway, documentNumberAvailableUseCase);
        verifyNoInteractions(emailAvailableUseCase);
    }

    @Test
    void shouldThrowEmailExceptionWhenEmailChangedAndUnavailable() throws Exception {
        UUID id = UUID.randomUUID();
        Customer oldC = customer(id, "A", "39053344705", "9999", "a@b.com");
        Customer updC = customer(id, "A", "39053344705", "9999", "x@y.com");
        when(customerGateway.findById(id)).thenReturn(Optional.of(oldC));
        when(emailAvailableUseCase.emailAvailable("x@y.com")).thenReturn(false);

        UpdateCustomerUseCaseImpl useCase = new UpdateCustomerUseCaseImpl(
                documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);

        assertThrows(EmailException.class, () -> useCase.execute(updC));
        InOrder inOrder = inOrder(customerGateway, emailAvailableUseCase);
        inOrder.verify(customerGateway).findById(id);
        inOrder.verify(emailAvailableUseCase).emailAvailable("x@y.com");
        verifyNoMoreInteractions(customerGateway, emailAvailableUseCase);
        verifyNoInteractions(documentNumberAvailableUseCase);
    }

    @Test
    void shouldThrowInternalErrorWhenGatewayReturnsNull() throws Exception {
        UUID id = UUID.randomUUID();
        Customer oldC = customer(id, "A", "39053344705", "9999", "a@b.com");
        Customer updC = customer(id, "A", "39053344705", "9999", "a@b.com");
        when(customerGateway.findById(id)).thenReturn(Optional.of(oldC));
        when(customerGateway.update(updC)).thenReturn(null);

        UpdateCustomerUseCaseImpl useCase = new UpdateCustomerUseCaseImpl(
                documentNumberAvailableUseCase, emailAvailableUseCase, customerGateway);

        assertThrows(InternalServerErrorException.class, () -> useCase.execute(updC));
        InOrder inOrder = inOrder(customerGateway);
        inOrder.verify(customerGateway).findById(id);
        inOrder.verify(customerGateway).update(updC);
        verifyNoMoreInteractions(customerGateway);
        verifyNoInteractions(documentNumberAvailableUseCase, emailAvailableUseCase);
    }
}
