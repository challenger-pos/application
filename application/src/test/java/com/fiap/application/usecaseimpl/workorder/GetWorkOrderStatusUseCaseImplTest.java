package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.customer.Customer;
import com.fiap.core.domain.customer.DocumentNumber;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.ForbiddenException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.UnauthorizedException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetWorkOrderStatusUseCaseImplTest {

    @Mock WorkOrderGateway workOrderGateway;
    @Mock WorkOrder workOrder;
    @Mock Customer customer;
    @Mock DocumentNumber documentNumberObj;

    @Test
    void shouldReturnStatusWhenWorkOrderExists() throws NotFoundException, ForbiddenException, UnauthorizedException {
        UUID id = UUID.randomUUID();
        String documentNumber = "01782982043";
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.COMPLETED);
        when(workOrder.getCustomer()).thenReturn(customer);
        when(customer.getDocumentNumber()).thenReturn(documentNumberObj);
        when(documentNumberObj.getValue()).thenReturn(documentNumber);

        GetWorkOrderStatusUseCaseImpl useCase = new GetWorkOrderStatusUseCaseImpl(workOrderGateway);

        WorkOrderStatus result = useCase.execute(id, documentNumber);

        assertEquals(WorkOrderStatus.COMPLETED, result);
        verify(workOrderGateway).findById(id);
        verify(workOrder).getStatus();
        verifyNoMoreInteractions(workOrderGateway, workOrder);
    }

    @Test
    void shouldThrowNotFoundWhenWorkOrderDoesNotExist() {
        UUID id = UUID.randomUUID();
        String documentNumber = "01782982043";
        when(workOrderGateway.findById(id)).thenReturn(Optional.empty());

        GetWorkOrderStatusUseCaseImpl useCase = new GetWorkOrderStatusUseCaseImpl(workOrderGateway);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> useCase.execute(id, documentNumber));
        assertEquals(ErrorCodeEnum.WORK0001.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.WORK0001.getMessage(), ex.getMessage());

        verify(workOrderGateway).findById(id);
        verifyNoMoreInteractions(workOrderGateway);
    }

    @Test
    void shouldThrowForbiddenWhenDocumentNumberDoesNotMatchWorkOrder() {
        UUID id = UUID.randomUUID();
        String requestDocumentNumber = "01782982043";
        String workOrderDocumentNumber = "12345678900";

        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getCustomer()).thenReturn(customer);
        when(customer.getDocumentNumber()).thenReturn(documentNumberObj);
        when(documentNumberObj.getValue()).thenReturn(workOrderDocumentNumber);

        GetWorkOrderStatusUseCaseImpl useCase = new GetWorkOrderStatusUseCaseImpl(workOrderGateway);

        ForbiddenException ex = assertThrows(ForbiddenException.class, () -> useCase.execute(id, requestDocumentNumber));
        assertEquals(ErrorCodeEnum.WORK0007.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.WORK0007.getMessage(), ex.getMessage());

        verify(workOrderGateway).findById(id);
        verify(workOrder).getCustomer();
        verify(customer).getDocumentNumber();
        verify(documentNumberObj).getValue();
        verifyNoMoreInteractions(workOrderGateway, workOrder, customer, documentNumberObj);
    }

    @Test
    void shouldThrowUnauthorizedWhenDocumentNumberIsNull() {
        UUID id = UUID.randomUUID();
        String documentNumber = null;

        GetWorkOrderStatusUseCaseImpl useCase = new GetWorkOrderStatusUseCaseImpl(workOrderGateway);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> useCase.execute(id, documentNumber));
        assertEquals(ErrorCodeEnum.WORK0008.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.WORK0008.getMessage(), ex.getMessage());

        verifyNoInteractions(workOrderGateway);
    }
}
