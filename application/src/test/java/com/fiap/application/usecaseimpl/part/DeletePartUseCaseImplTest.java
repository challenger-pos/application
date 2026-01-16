package com.fiap.application.usecaseimpl.part;

import com.fiap.application.gateway.part.PartGateway;
import com.fiap.application.gateway.part.WorkOrderPartGateway;
import com.fiap.core.exception.BusinessRuleException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.part.DeletePartUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletePartUseCaseImplTest {

    @Mock
    PartGateway partGateway;

    @Mock
    WorkOrderPartGateway workOrderPartGateway;

    @Test
    void shouldThrowNotFoundWhenPartDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(partGateway.existsById(id)).thenReturn(false);

        DeletePartUseCase useCase = new DeletePartUseCaseImpl(partGateway, workOrderPartGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));

        verify(partGateway).existsById(id);
        verifyNoInteractions(workOrderPartGateway);
        verify(partGateway, never()).delete(any());
        verifyNoMoreInteractions(partGateway);
    }

    @Test
    void shouldBlockDeleteWhenPartIsUsedInWorkOrders() {
        UUID id = UUID.randomUUID();
        when(partGateway.existsById(id)).thenReturn(true);
        when(workOrderPartGateway.existsByPartId(id)).thenReturn(true);

        DeletePartUseCase useCase = new DeletePartUseCaseImpl(partGateway, workOrderPartGateway);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(id));

        InOrder inOrder = inOrder(partGateway, workOrderPartGateway);
        inOrder.verify(partGateway).existsById(id);
        inOrder.verify(workOrderPartGateway).existsByPartId(id);
        verify(partGateway, never()).delete(any());
        verifyNoMoreInteractions(partGateway, workOrderPartGateway);
    }

    @Test
    void shouldDeleteWhenExistsAndNotReferenced() throws NotFoundException, BusinessRuleException {
        UUID id = UUID.randomUUID();
        when(partGateway.existsById(id)).thenReturn(true);
        when(workOrderPartGateway.existsByPartId(id)).thenReturn(false);

        DeletePartUseCase useCase = new DeletePartUseCaseImpl(partGateway, workOrderPartGateway);

        useCase.execute(id);

        InOrder inOrder = inOrder(partGateway, workOrderPartGateway);
        inOrder.verify(partGateway).existsById(id);
        inOrder.verify(workOrderPartGateway).existsByPartId(id);
        inOrder.verify(partGateway).delete(id);
        verifyNoMoreInteractions(partGateway, workOrderPartGateway);
    }
}
