package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrder;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateStatusWorkOrderUseCaseImplTest {

    @Mock WorkOrderGateway workOrderGateway;
    @Mock WorkOrder workOrder;
    @Mock WorkOrder updated;

    @Test
    void shouldThrowNotFoundWhenWorkOrderMissing() {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.empty());

        UpdateStatusWorkOrderUseCaseImpl useCase = new UpdateStatusWorkOrderUseCaseImpl(workOrderGateway);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> useCase.execute(id, "IN_PROGRESS"));
        assert ex.getCode().equals(ErrorCodeEnum.WORK0001.getCode());

        verify(workOrderGateway).findById(id);
        verifyNoMoreInteractions(workOrderGateway);
    }

    @Test
    void shouldThrowBadRequestWhenStatusStringInvalid() {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));

        UpdateStatusWorkOrderUseCaseImpl useCase = new UpdateStatusWorkOrderUseCaseImpl(workOrderGateway);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(id, "INVALID_STATUS"));
        assert ex.getCode().equals(ErrorCodeEnum.WORK0004.getCode());

        verify(workOrderGateway).findById(id);
        verifyNoMoreInteractions(workOrderGateway);
        verifyNoInteractions(workOrder);
    }

    @Test
    void shouldThrowBadRequestWhenNewStatusEqualsCurrent() {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.IN_PROGRESS);

        UpdateStatusWorkOrderUseCaseImpl useCase = new UpdateStatusWorkOrderUseCaseImpl(workOrderGateway);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(id, "IN_PROGRESS"));
        assert ex.getCode().equals(ErrorCodeEnum.WORK0005.getCode());

        verify(workOrderGateway).findById(id);
        verify(workOrder).getStatus();
        verifyNoMoreInteractions(workOrderGateway, workOrder);
    }

    @Test
    void shouldSetFinishedAtWhenDeliveredAndPersist() throws Exception {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.IN_PROGRESS);
        when(workOrderGateway.update(workOrder)).thenReturn(updated);

        UpdateStatusWorkOrderUseCaseImpl useCase = new UpdateStatusWorkOrderUseCaseImpl(workOrderGateway);

        WorkOrder result = useCase.execute(id, "DELIVERED");

        assertSame(updated, result);

        InOrder inOrder = inOrder(workOrderGateway, workOrder);
        inOrder.verify(workOrderGateway).findById(id);
        inOrder.verify(workOrder).getStatus();
        inOrder.verify(workOrder).setFinishedAt(any(LocalDateTime.class));
        inOrder.verify(workOrder).setStatus(WorkOrderStatus.DELIVERED);
        inOrder.verify(workOrder).setUpdatedAt(any(LocalDateTime.class));
        inOrder.verify(workOrderGateway).update(workOrder);
        verifyNoMoreInteractions(workOrderGateway, workOrder);
    }

    @Test
    void shouldSetFinishedAtWhenCompletedAndPersist() throws Exception {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.IN_PROGRESS);
        when(workOrderGateway.update(workOrder)).thenReturn(updated);

        UpdateStatusWorkOrderUseCaseImpl useCase = new UpdateStatusWorkOrderUseCaseImpl(workOrderGateway);

        WorkOrder result = useCase.execute(id, "COMPLETED");

        assertSame(updated, result);

        InOrder inOrder = inOrder(workOrderGateway, workOrder);
        inOrder.verify(workOrderGateway).findById(id);
        inOrder.verify(workOrder).getStatus();
        inOrder.verify(workOrder).setFinishedAt(any(LocalDateTime.class));
        inOrder.verify(workOrder).setStatus(WorkOrderStatus.COMPLETED);
        inOrder.verify(workOrder).setUpdatedAt(any(LocalDateTime.class));
        inOrder.verify(workOrderGateway).update(workOrder);
        verifyNoMoreInteractions(workOrderGateway, workOrder);
    }

    @Test
    void shouldUpdateWithoutFinishedAtForNonTerminalStatuses() throws Exception {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.RECEIVED);
        when(workOrderGateway.update(workOrder)).thenReturn(updated);

        UpdateStatusWorkOrderUseCaseImpl useCase = new UpdateStatusWorkOrderUseCaseImpl(workOrderGateway);

        WorkOrder result = useCase.execute(id, "IN_DIAGNOSIS");

        assertSame(updated, result);

        InOrder inOrder = inOrder(workOrderGateway, workOrder);
        inOrder.verify(workOrderGateway).findById(id);
        inOrder.verify(workOrder).getStatus();
        inOrder.verify(workOrder, never()).setFinishedAt(any(LocalDateTime.class));
        inOrder.verify(workOrder).setStatus(WorkOrderStatus.IN_DIAGNOSIS);
        inOrder.verify(workOrder).setUpdatedAt(any(LocalDateTime.class));
        inOrder.verify(workOrderGateway).update(workOrder);
        verifyNoMoreInteractions(workOrderGateway, workOrder);
    }
}
