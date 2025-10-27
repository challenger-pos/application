package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.part.PartGateway;
import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.part.Part;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderPart;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.BadRequestException;
import com.fiap.core.exception.NotFoundException;
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
        when(workOrderGateway.findById(id)).thenReturn(Optional.empty());

        ApproveWorkOrderUseCaseImpl useCase = new ApproveWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));

        verify(workOrderGateway).findById(id);
        verifyNoMoreInteractions(workOrderGateway, partGateway);
    }

    @Test
    void shouldThrowBadRequestWhenStatusIsNotAwaitingApproval() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.RECEIVED);

        ApproveWorkOrderUseCaseImpl useCase = new ApproveWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(id));
        assert ex.getCode().equals(ErrorCodeEnum.WORK0006.getCode());

        verify(workOrderGateway).findById(id);
        verify(workOrder).getStatus();
        verifyNoMoreInteractions(workOrderGateway, partGateway);
    }

    @Test
    void shouldApproveAndPersistWithPartsSaved() throws Exception {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.AWAITING_APPROVAL);
        when(wop1.getPart()).thenReturn(part1);
        when(wop2.getPart()).thenReturn(part2);
        when(workOrder.getWorkOrderParts()).thenReturn(List.of(wop1, wop2));

        ApproveWorkOrderUseCaseImpl useCase = new ApproveWorkOrderUseCaseImpl(workOrderGateway, partGateway);

        useCase.execute(id);

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
}
