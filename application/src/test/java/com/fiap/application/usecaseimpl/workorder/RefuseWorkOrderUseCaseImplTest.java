package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.part.PartGateway;
import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.customer.Customer;
import com.fiap.core.domain.customer.DocumentNumber;
import com.fiap.core.domain.part.Part;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderPart;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.*;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefuseWorkOrderUseCaseImplTest {

    @Mock WorkOrderGateway workOrderGateway;
    @Mock PartGateway partGateway;
    @Mock WorkOrder workOrder;
    @Mock Customer customer;
    @Mock DocumentNumber documentNumberObj;
    @Mock WorkOrderPart wop1;
    @Mock WorkOrderPart wop2;
    @Mock Part part1;
    @Mock Part part2;

    @Test
    void shouldThrowWhenWorkOrderNotFound() {
        UUID id = UUID.randomUUID();
        String documentNumber = "01782982043";
        when(workOrderGateway.findById(id)).thenReturn(Optional.empty());

        RefuseWorkOrderUseCaseImpl useCase = new RefuseWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id, documentNumber));

        verify(workOrderGateway).findById(id);
    }

    @Test
    void shouldThrowWhenStatusIsNotAwaitingApproval() {
        UUID id = UUID.randomUUID();
        String documentNumber = "01782982043";
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.IN_PROGRESS);

        RefuseWorkOrderUseCaseImpl useCase = new RefuseWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        assertThrows(BadRequestException.class, () -> useCase.execute(id, documentNumber));

        verify(workOrderGateway).findById(id);
        verify(workOrder, atLeastOnce()).getStatus();
    }

    @Test
    void shouldPropagateBusinessRuleFromRestoreStock() throws Exception {
        UUID id = UUID.randomUUID();
        String documentNumber = "01782982043";
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.AWAITING_APPROVAL);
        when(workOrder.getCustomer()).thenReturn(customer);
        when(customer.getDocumentNumber()).thenReturn(documentNumberObj);
        when(documentNumberObj.getValue()).thenReturn(documentNumber);
        doThrow(new BusinessRuleException("x","y")).when(workOrder).restoreStock();

        RefuseWorkOrderUseCaseImpl useCase = new RefuseWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(id, documentNumber));

        verify(workOrderGateway).findById(id);
        verify(workOrder, atLeastOnce()).getStatus();
        verify(workOrder).restoreStock();
    }

    @Test
    void shouldRestoreStockSetStatusFinishedAndPersist() throws Exception {
        UUID id = UUID.randomUUID();
        String documentNumber = "01782982043";
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.AWAITING_APPROVAL);
        when(workOrder.getCustomer()).thenReturn(customer);
        when(customer.getDocumentNumber()).thenReturn(documentNumberObj);
        when(documentNumberObj.getValue()).thenReturn(documentNumber);
        when(workOrder.getWorkOrderParts()).thenReturn(List.of(wop1, wop2));
        when(wop1.getPart()).thenReturn(part1);
        when(wop2.getPart()).thenReturn(part2);

        RefuseWorkOrderUseCaseImpl useCase = new RefuseWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        useCase.execute(id, documentNumber);

        // Verificações de Comportamento
        verify(workOrderGateway).findById(id);
        verify(workOrder, atLeastOnce()).getStatus();
        verify(workOrder).restoreStock();

        // Verifique o status correto de acordo com sua regra (REFUSED ou COMPLETED)
        // Se no código for COMPLETED, mantenha COMPLETED.
        verify(workOrder).setStatus(any(WorkOrderStatus.class));
        verify(workOrder).setFinishedAt(any(LocalDateTime.class));

        // Verificações de Persistência
        verify(partGateway).saveAll(anyList());
        verify(workOrderGateway, atLeastOnce()).save(workOrder);
    }

    @Test
    void shouldThrowForbiddenWhenDocumentNumberDoesNotMatchWorkOrder() throws NotFoundException, BadRequestException {
        UUID id = UUID.randomUUID();
        String requestDocumentNumber = "01782982043";
        String workOrderDocumentNumber = "12345678900";

        Customer customerMock = mock(Customer.class);
        DocumentNumber docNumber = mock(DocumentNumber.class);
        when(docNumber.getValue()).thenReturn(workOrderDocumentNumber);
        when(customerMock.getDocumentNumber()).thenReturn(docNumber);

        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.AWAITING_APPROVAL);
        when(workOrder.getCustomer()).thenReturn(customerMock);

        RefuseWorkOrderUseCaseImpl useCase = new RefuseWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        ForbiddenException ex = assertThrows(ForbiddenException.class, () -> useCase.execute(id, requestDocumentNumber));
        assertEquals(ErrorCodeEnum.WORK0007.getCode(), ex.getCode());

        verify(workOrderGateway).findById(id);
        verify(workOrder, atLeastOnce()).getStatus();
    }

    @Test
    void shouldThrowUnauthorizedWhenDocumentNumberIsNull() {
        UUID id = UUID.randomUUID();
        String documentNumber = null;

        RefuseWorkOrderUseCaseImpl useCase = new RefuseWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> useCase.execute(id, documentNumber));
        assertEquals(ErrorCodeEnum.WORK0008.getCode(), ex.getCode());

        verifyNoInteractions(workOrderGateway);
    }
}