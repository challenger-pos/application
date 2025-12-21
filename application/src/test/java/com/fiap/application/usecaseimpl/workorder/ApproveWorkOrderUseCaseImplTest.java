package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.part.PartGateway;
import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.customer.Customer;
import com.fiap.core.domain.customer.DocumentNumber;
import com.fiap.core.domain.part.Part;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderPart;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.BadRequestException;
import com.fiap.core.exception.ForbiddenException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.UnauthorizedException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
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
class ApproveWorkOrderUseCaseImplTest {

    @Mock
    WorkOrderGateway workOrderGateway;

    @Mock
    PartGateway partGateway;

    @Mock
    WorkOrder workOrder;

    @Mock
    Customer customer;

    @Mock
    DocumentNumber documentNumberObj;

    @Mock
    WorkOrderPart wop1;

    @Mock
    WorkOrderPart wop2;

    @Mock
    Part part1;

    @Mock
    Part part2;

    @Test
    void shouldThrowNotFoundWhenWorkOrderDoesNotExist() {
        UUID id = UUID.randomUUID();
        String documentNumber = "01782982043";
        when(workOrderGateway.findById(id)).thenReturn(Optional.empty());

        ApproveWorkOrderUseCaseImpl useCase = new ApproveWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id, documentNumber));

        verify(workOrderGateway).findById(id);
        verifyNoMoreInteractions(workOrderGateway, partGateway);
    }

    @Test
    void shouldThrowBadRequestWhenStatusIsNotAwaitingApproval() throws NotFoundException {
        UUID id = UUID.randomUUID();
        String documentNumber = "01782982043";
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.RECEIVED);

        ApproveWorkOrderUseCaseImpl useCase = new ApproveWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(id, documentNumber));
        assert ex.getCode().equals(ErrorCodeEnum.WORK0006.getCode());

        verify(workOrderGateway).findById(id);
        verify(workOrder).getStatus();
        verifyNoMoreInteractions(workOrderGateway, partGateway);
    }

    @Test
    void shouldApproveAndPersistWithPartsSaved() throws Exception {
        UUID id = UUID.randomUUID();
        String documentNumber = "01782982043";
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.AWAITING_APPROVAL);
        when(workOrder.getCustomer()).thenReturn(customer);
        when(customer.getDocumentNumber()).thenReturn(documentNumberObj);
        when(documentNumberObj.getValue()).thenReturn(documentNumber);
        when(wop1.getPart()).thenReturn(part1);
        when(wop2.getPart()).thenReturn(part2);
        when(workOrder.getWorkOrderParts()).thenReturn(List.of(wop1, wop2));

        ApproveWorkOrderUseCaseImpl useCase = new ApproveWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        useCase.execute(id, documentNumber);

        InOrder inOrder = inOrder(workOrderGateway, workOrder, partGateway);
        inOrder.verify(workOrderGateway).findById(id);
        inOrder.verify(workOrder).getStatus();
        inOrder.verify(workOrder).approveStock();
        inOrder.verify(workOrder).setStatus(WorkOrderStatus.IN_PROGRESS);
        inOrder.verify(workOrder).setApprovedAt(any(LocalDateTime.class));
        inOrder.verify(workOrder).getWorkOrderParts();
        inOrder.verify(partGateway).saveAll(List.of(part1, part2));
        inOrder.verify(workOrderGateway).save(workOrder);

        verifyNoMoreInteractions(workOrderGateway, partGateway);
    }

    @Test
    void shouldThrowForbiddenWhenDocumentNumberDoesNotMatchWorkOrder() throws NotFoundException, BadRequestException {
        UUID id = UUID.randomUUID();
        String requestDocumentNumber = "01782982043";
        String workOrderDocumentNumber = "12345678900";

        Customer customer = mock(Customer.class);
        DocumentNumber docNumber = mock(DocumentNumber.class);
        when(docNumber.getValue()).thenReturn(workOrderDocumentNumber);
        when(customer.getDocumentNumber()).thenReturn(docNumber);

        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.AWAITING_APPROVAL);
        when(workOrder.getCustomer()).thenReturn(customer);

        ApproveWorkOrderUseCaseImpl useCase = new ApproveWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        ForbiddenException ex = assertThrows(ForbiddenException.class, () -> useCase.execute(id, requestDocumentNumber));
        assert ex.getCode().equals(ErrorCodeEnum.WORK0007.getCode());

        verify(workOrderGateway).findById(id);
        verify(workOrder).getStatus();
        verify(workOrder).getCustomer();
        verify(docNumber).getValue();
        verifyNoMoreInteractions(workOrderGateway, workOrder, partGateway);
    }

    @Test
    void shouldThrowUnauthorizedWhenDocumentNumberIsNull() {
        UUID id = UUID.randomUUID();
        String documentNumber = null;

        ApproveWorkOrderUseCaseImpl useCase = new ApproveWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> useCase.execute(id, documentNumber));
        assertEquals(ErrorCodeEnum.WORK0008.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.WORK0008.getMessage(), ex.getMessage());

        verifyNoInteractions(workOrderGateway);
    }
}
