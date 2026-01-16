package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculateAverageTimeWorkOrderUseCaseImplTest {

    @Mock WorkOrderGateway workOrderGateway;
    @Mock WorkOrder w1;
    @Mock WorkOrder w2;
    @Mock WorkOrder w3;

    @Test
    void shouldAverageUsingApprovedOrCreatedAndRound() {
        LocalDateTime base = LocalDateTime.of(2025, 10, 13, 12, 0);

        when(workOrderGateway.findByStatus(WorkOrderStatus.COMPLETED)).thenReturn(List.of(w1, w2));

        when(w1.getApprovedAt()).thenReturn(base);
        when(w1.getFinishedAt()).thenReturn(base.plusMinutes(95));

        when(w2.getApprovedAt()).thenReturn(base.plusHours(3));
        when(w2.getFinishedAt()).thenReturn(base.plusHours(3).plusMinutes(25));

        CalculateAverageTimeWorkOrderUseCaseImpl useCase =
                new CalculateAverageTimeWorkOrderUseCaseImpl(workOrderGateway);

        String result = useCase.execute();

        assertEquals("01h 00m", result);
        verify(workOrderGateway).findByStatus(WorkOrderStatus.COMPLETED);
        verifyNoMoreInteractions(workOrderGateway);
    }

    @Test
    void shouldUseCreatedWhenApprovedIsNullAndIgnoreWithoutFinished() {
        LocalDateTime base = LocalDateTime.of(2025, 10, 13, 12, 0);

        when(workOrderGateway.findByStatus(WorkOrderStatus.COMPLETED)).thenReturn(List.of(w1, w2, w3));

        when(w1.getApprovedAt()).thenReturn(null);
        when(w1.getCreatedAt()).thenReturn(base);
        when(w1.getFinishedAt()).thenReturn(base.plusMinutes(30));

        when(w2.getApprovedAt()).thenReturn(base.plusMinutes(10));
        when(w2.getFinishedAt()).thenReturn(base.plusMinutes(40));

        when(w3.getFinishedAt()).thenReturn(null);

        CalculateAverageTimeWorkOrderUseCaseImpl useCase =
                new CalculateAverageTimeWorkOrderUseCaseImpl(workOrderGateway);

        String result = useCase.execute();

        assertEquals("00h 30m", result);
        verify(workOrderGateway).findByStatus(WorkOrderStatus.COMPLETED);
        verifyNoMoreInteractions(workOrderGateway);
    }

    @Test
    void shouldReturnZerosWhenNoDurations() {
        when(workOrderGateway.findByStatus(WorkOrderStatus.COMPLETED)).thenReturn(List.of());

        CalculateAverageTimeWorkOrderUseCaseImpl useCase =
                new CalculateAverageTimeWorkOrderUseCaseImpl(workOrderGateway);

        assertEquals("00h 00m", useCase.execute());
        verify(workOrderGateway).findByStatus(WorkOrderStatus.COMPLETED);
        verifyNoMoreInteractions(workOrderGateway);
    }
}
