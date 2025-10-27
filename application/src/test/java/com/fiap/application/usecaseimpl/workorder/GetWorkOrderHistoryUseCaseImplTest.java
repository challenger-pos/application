package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrderHistory;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetWorkOrderHistoryUseCaseImplTest {

    WorkOrderGateway workOrderGateway = mock(WorkOrderGateway.class);

    @Test
    void shouldReturnHistoriesSortedByCreatedAtAsc() throws NotFoundException {
        String doc = "123";
        var h1 = new WorkOrderHistory(UUID.randomUUID(), null, WorkOrderStatus.RECEIVED, null, LocalDateTime.now().minusDays(2));
        var h2 = new WorkOrderHistory(UUID.randomUUID(), null, WorkOrderStatus.IN_PROGRESS, null, LocalDateTime.now().minusDays(3));
        var h3 = new WorkOrderHistory(UUID.randomUUID(), null, WorkOrderStatus.COMPLETED, null, LocalDateTime.now().minusDays(1));

        when(workOrderGateway.getHistoryByCustomerCpfCnpj(doc))
                .thenReturn(new java.util.ArrayList<>(java.util.List.of(h1, h2, h3)));

        var useCase = new GetWorkOrderHistoryUseCaseImpl(workOrderGateway);
        List<WorkOrderHistory> result = useCase.execute(doc);

        assertEquals(3, result.size());
        assertSame(h2, result.get(0));
        assertSame(h1, result.get(1));
        assertSame(h3, result.get(2));

        verify(workOrderGateway).getHistoryByCustomerCpfCnpj(doc);
        verifyNoMoreInteractions(workOrderGateway);
    }

    @Test
    void shouldThrowWhenNoHistoryFound_emptyList() {
        String doc = "123";
        when(workOrderGateway.getHistoryByCustomerCpfCnpj(doc)).thenReturn(List.of());

        var useCase = new GetWorkOrderHistoryUseCaseImpl(workOrderGateway);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> useCase.execute(doc));
        assertEquals(ErrorCodeEnum.HIST0001.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.HIST0001.getMessage(), ex.getMessage());

        verify(workOrderGateway).getHistoryByCustomerCpfCnpj(doc);
        verifyNoMoreInteractions(workOrderGateway);
    }

    @Test
    void shouldThrowWhenNoHistoryFound_nullFromGateway() {
        String doc = "123";
        when(workOrderGateway.getHistoryByCustomerCpfCnpj(doc)).thenReturn(null);

        var useCase = new GetWorkOrderHistoryUseCaseImpl(workOrderGateway);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> useCase.execute(doc));
        assertEquals(ErrorCodeEnum.HIST0001.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.HIST0001.getMessage(), ex.getMessage());

        verify(workOrderGateway).getHistoryByCustomerCpfCnpj(doc);
        verifyNoMoreInteractions(workOrderGateway);
    }
}
