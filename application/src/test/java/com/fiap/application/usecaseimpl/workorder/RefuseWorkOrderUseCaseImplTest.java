package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.part.PartGateway;
import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.part.Part;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderPart;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.BadRequestException;
import com.fiap.core.exception.BusinessRuleException;
import com.fiap.core.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefuseWorkOrderUseCaseImplTest {

    @Mock WorkOrderGateway workOrderGateway;
    @Mock PartGateway partGateway;
    @Mock WorkOrder workOrder;
    @Mock WorkOrderPart wop1;
    @Mock WorkOrderPart wop2;
    @Mock Part part1;
    @Mock Part part2;

    @Test
    void shouldThrowWhenWorkOrderNotFound() {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.empty());

        RefuseWorkOrderUseCaseImpl useCase = new RefuseWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));

        verify(workOrderGateway).findById(id);
        verifyNoMoreInteractions(workOrderGateway, partGateway);
    }

    @Test
    void shouldThrowWhenStatusIsNotAwaitingApproval() {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.IN_PROGRESS);

        RefuseWorkOrderUseCaseImpl useCase = new RefuseWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        assertThrows(BadRequestException.class, () -> useCase.execute(id));

        verify(workOrderGateway).findById(id);
        verify(workOrder).getStatus();
        verifyNoMoreInteractions(workOrderGateway, partGateway);
        verifyNoMoreInteractions(workOrder);
    }

    @Test
    void shouldPropagateBusinessRuleFromRestoreStock() throws Exception {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.AWAITING_APPROVAL);
        doThrow(new BusinessRuleException("x","y")).when(workOrder).restoreStock();

        RefuseWorkOrderUseCaseImpl useCase = new RefuseWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(id));

        verify(workOrderGateway).findById(id);
        verify(workOrder).getStatus();
        verify(workOrder).restoreStock();
        verifyNoMoreInteractions(workOrderGateway, partGateway, workOrder);
    }

    @Test
    void shouldRestoreStockSetStatusFinishedAndPersist() throws Exception {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.AWAITING_APPROVAL);
        when(workOrder.getWorkOrderParts()).thenReturn(List.of(wop1, wop2));
        when(wop1.getPart()).thenReturn(part1);
        when(wop2.getPart()).thenReturn(part2);

        RefuseWorkOrderUseCaseImpl useCase = new RefuseWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        useCase.execute(id);

        InOrder inOrder = inOrder(workOrderGateway, workOrder, partGateway);
        inOrder.verify(workOrderGateway).findById(id);
        inOrder.verify(workOrder).getStatus();
        inOrder.verify(workOrder).restoreStock();
        inOrder.verify(workOrder).setStatus(WorkOrderStatus.COMPLETED);
        inOrder.verify(workOrder).setFinishedAt(any(LocalDateTime.class));
        inOrder.verify(workOrder).getWorkOrderParts();
        inOrder.verify(partGateway).saveAll(List.of(part1, part2));
        inOrder.verify(workOrderGateway).save(workOrder);
        verifyNoMoreInteractions(workOrderGateway, partGateway, workOrder);
    }
}
