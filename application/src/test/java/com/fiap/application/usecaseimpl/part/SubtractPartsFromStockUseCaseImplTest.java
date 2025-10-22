package com.fiap.application.usecaseimpl.part;

import com.fiap.core.domain.part.Part;
import com.fiap.core.exception.BusinessRuleException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.part.FindPartByIdUseCase;
import com.fiap.usecase.part.UpdatePartUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubtractPartsFromStockUseCaseImplTest {

    @Mock
    FindPartByIdUseCase findPartByIdUseCase;

    @Mock
    UpdatePartUseCase updatePartUseCase;

    @Mock
    Part part;

    @Test
    void shouldSubtractPartsFromStockAndUpdate() throws NotFoundException, BusinessRuleException {
        UUID id = UUID.randomUUID();
        int qty = 3;

        when(findPartByIdUseCase.execute(id)).thenReturn(part);

        SubtractPartsFromStockUseCaseImpl useCase =
                new SubtractPartsFromStockUseCaseImpl(findPartByIdUseCase, updatePartUseCase);

        useCase.execute(id, qty);

        InOrder inOrder = inOrder(findPartByIdUseCase, part, updatePartUseCase);
        inOrder.verify(findPartByIdUseCase).execute(id);
        inOrder.verify(part).subtractFromStock(qty);
        inOrder.verify(updatePartUseCase).execute(part);
        verifyNoMoreInteractions(findPartByIdUseCase, part, updatePartUseCase);
    }

    @Test
    void shouldPropagateNotFoundWhenPartDoesNotExist() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(findPartByIdUseCase.execute(id)).thenThrow(new NotFoundException("x", "y"));

        SubtractPartsFromStockUseCaseImpl useCase =
                new SubtractPartsFromStockUseCaseImpl(findPartByIdUseCase, updatePartUseCase);

        assertThrows(NotFoundException.class, () -> useCase.execute(id, 3));

        verify(findPartByIdUseCase).execute(id);
        verifyNoInteractions(part, updatePartUseCase);
    }

    @Test
    void shouldPropagateBusinessRuleExceptionFromPart() throws NotFoundException, BusinessRuleException {
        UUID id = UUID.randomUUID();
        when(findPartByIdUseCase.execute(id)).thenReturn(part);
        doThrow(new BusinessRuleException("x", "y")).when(part).subtractFromStock(3);

        SubtractPartsFromStockUseCaseImpl useCase =
                new SubtractPartsFromStockUseCaseImpl(findPartByIdUseCase, updatePartUseCase);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(id, 3));

        verify(findPartByIdUseCase).execute(id);
        verify(part).subtractFromStock(3);
        verifyNoInteractions(updatePartUseCase);
    }
}
