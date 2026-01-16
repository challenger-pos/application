package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindWorkOrderByIdUseCaseImplTest {

    @Mock WorkOrderGateway workOrderGateway;
    @Mock WorkOrder workOrder;

    @Test
    void shouldReturnWorkOrderWhenFound() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.of(workOrder));

        FindWorkOrderByIdUseCaseImpl useCase = new FindWorkOrderByIdUseCaseImpl(workOrderGateway);

        WorkOrder result = useCase.execute(id);

        assertSame(workOrder, result);
        verify(workOrderGateway).findById(id);
        verifyNoMoreInteractions(workOrderGateway);
    }

    @Test
    void shouldThrowNotFoundWhenMissing() {
        UUID id = UUID.randomUUID();
        when(workOrderGateway.findById(id)).thenReturn(Optional.empty());

        FindWorkOrderByIdUseCaseImpl useCase = new FindWorkOrderByIdUseCaseImpl(workOrderGateway);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> useCase.execute(id));
        assert ex.getCode().equals(ErrorCodeEnum.WORK0001.getCode());
        assert ex.getMessage().equals(ErrorCodeEnum.WORK0001.getMessage());

        verify(workOrderGateway).findById(id);
        verifyNoMoreInteractions(workOrderGateway);
    }
}
