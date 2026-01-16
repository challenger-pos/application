package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.service.ServiceGateway;
import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.BadRequestException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    @Mock ServiceGateway serviceGateway;
    @Mock WorkOrder workOrder;
    @Mock WorkOrder updated;

    @Test
    void shouldThrowNotFoundWhenWorkOrderMissing() {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.empty());

        UpdateStatusWorkOrderUseCaseImpl useCase = new UpdateStatusWorkOrderUseCaseImpl(workOrderGateway, serviceGateway);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> useCase.execute(id, "IN_PROGRESS"));
        assert ex.getCode().equals(ErrorCodeEnum.WORK0001.getCode());

        verify(workOrderGateway).findById(id);
    }

    @Test
    void shouldThrowBadRequestWhenStatusStringInvalid() {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));

        UpdateStatusWorkOrderUseCaseImpl useCase = new UpdateStatusWorkOrderUseCaseImpl(workOrderGateway, serviceGateway);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(id, "INVALID_STATUS"));
        assert ex.getCode().equals(ErrorCodeEnum.WORK0004.getCode());

        verify(workOrderGateway).findById(id);
    }

    @Test
    void shouldThrowBadRequestWhenNewStatusEqualsCurrent() {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.IN_PROGRESS);

        UpdateStatusWorkOrderUseCaseImpl useCase = new UpdateStatusWorkOrderUseCaseImpl(workOrderGateway, serviceGateway);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(id, "IN_PROGRESS"));
        assert ex.getCode().equals(ErrorCodeEnum.WORK0005.getCode());

        verify(workOrderGateway).findById(id);
        verify(workOrder, atLeastOnce()).getStatus();
    }

    @Test
    void shouldSetFinishedAtWhenDeliveredAndPersist() throws Exception {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.IN_PROGRESS);
        // Usando atLeastOnce no retorno do mock caso o gateway seja chamado em momentos de transição
        when(workOrderGateway.update(any())).thenReturn(updated);

        UpdateStatusWorkOrderUseCaseImpl useCase = new UpdateStatusWorkOrderUseCaseImpl(workOrderGateway, serviceGateway);

        WorkOrder result = useCase.execute(id, "DELIVERED");

        assertSame(updated, result);

        verify(workOrderGateway).findById(id);
        verify(workOrder, atLeastOnce()).getStatus();
        verify(workOrder).setFinishedAt(any(LocalDateTime.class));
        verify(workOrder).setStatus(WorkOrderStatus.DELIVERED);
        verify(workOrderGateway, atLeastOnce()).update(workOrder);
    }

    @Test
    void shouldSetFinishedAtWhenCompletedAndPersist() throws Exception {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.IN_PROGRESS);
        when(workOrderGateway.update(any())).thenReturn(updated);

        UpdateStatusWorkOrderUseCaseImpl useCase = new UpdateStatusWorkOrderUseCaseImpl(workOrderGateway, serviceGateway);

        WorkOrder result = useCase.execute(id, "COMPLETED");

        assertSame(updated, result);

        verify(workOrderGateway).findById(id);
        verify(workOrder, atLeastOnce()).getStatus();
        verify(workOrder).setFinishedAt(any(LocalDateTime.class));
        verify(workOrder).setStatus(WorkOrderStatus.COMPLETED);
        verify(workOrderGateway, atLeastOnce()).update(workOrder);
    }

    @Test
    void shouldUpdateWithoutFinishedAtForNonTerminalStatuses() throws Exception {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.RECEIVED);
        when(workOrderGateway.update(any())).thenReturn(updated);

        UpdateStatusWorkOrderUseCaseImpl useCase = new UpdateStatusWorkOrderUseCaseImpl(workOrderGateway, serviceGateway);

        WorkOrder result = useCase.execute(id, "IN_DIAGNOSIS");

        assertSame(updated, result);

        verify(workOrderGateway).findById(id);
        verify(workOrder, atLeastOnce()).getStatus();
        verify(workOrder, never()).setFinishedAt(any(LocalDateTime.class));
        verify(workOrder).setStatus(WorkOrderStatus.IN_DIAGNOSIS);
        verify(workOrderGateway, atLeastOnce()).update(workOrder);
    }
}