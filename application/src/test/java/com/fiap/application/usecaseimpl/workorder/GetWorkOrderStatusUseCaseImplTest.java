package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetWorkOrderStatusUseCaseImplTest {

    @Mock WorkOrderGateway workOrderGateway;
    @Mock WorkOrder workOrder;

    @Test
    void shouldReturnStatusWhenWorkOrderExists() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));
        when(workOrder.getStatus()).thenReturn(WorkOrderStatus.COMPLETED);

        GetWorkOrderStatusUseCaseImpl useCase = new GetWorkOrderStatusUseCaseImpl(workOrderGateway);

        WorkOrderStatus result = useCase.execute(id);

        assertEquals(WorkOrderStatus.COMPLETED, result);
        verify(workOrderGateway).findById(id);
        verify(workOrder).getStatus();
        verifyNoMoreInteractions(workOrderGateway, workOrder);
    }

    @Test
    void shouldThrowNotFoundWhenWorkOrderDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.empty());

        GetWorkOrderStatusUseCaseImpl useCase = new GetWorkOrderStatusUseCaseImpl(workOrderGateway);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> useCase.execute(id));
        assertEquals(ErrorCodeEnum.WORK0001.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.WORK0001.getMessage(), ex.getMessage());

        verify(workOrderGateway).findById(id);
        verifyNoMoreInteractions(workOrderGateway);
    }
}
