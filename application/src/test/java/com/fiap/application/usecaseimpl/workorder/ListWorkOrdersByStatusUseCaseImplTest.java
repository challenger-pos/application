package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListWorkOrdersByStatusUseCaseImplTest {

    @Mock WorkOrderGateway workOrderGateway;
    @Mock WorkOrder w1;
    @Mock WorkOrder w2;

    @Test
    void shouldReturnWorkOrdersFromGateway() {
        List<WorkOrder> expected = List.of(w1, w2);
        when(workOrderGateway.findAllOrdered(
                List.of(WorkOrderStatus.COMPLETED, WorkOrderStatus.DELIVERED, WorkOrderStatus.REFUSED))
        ).thenReturn(expected);

        ListWorkOrdersByStatusUseCaseImpl useCase = new ListWorkOrdersByStatusUseCaseImpl(workOrderGateway);
        List<WorkOrder> result = useCase.execute();

        assertSame(expected, result);
        verify(workOrderGateway).findAllOrdered(
                List.of(WorkOrderStatus.COMPLETED, WorkOrderStatus.DELIVERED, WorkOrderStatus.REFUSED)
        );
        verifyNoMoreInteractions(workOrderGateway);
    }
}
