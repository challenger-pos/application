package com.fiap.application.usecaseimpl.part;

import com.fiap.application.gateway.part.PartGateway;
import com.fiap.core.domain.part.Part;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.part.FindPartByIdUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindPartByIdUseCaseImplTest {

    @Mock
    PartGateway partGateway;

    @Mock
    Part part;

    @Test
    void shouldReturnPartWhenFound() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(partGateway.findById(id)).thenReturn(Optional.of(part));

        FindPartByIdUseCase useCase = new FindPartByIdUseCaseImpl(partGateway);
        Part result = useCase.execute(id);

        assertSame(part, result);

        InOrder inOrder = inOrder(partGateway);
        inOrder.verify(partGateway).findById(id);
        verifyNoMoreInteractions(partGateway);
    }

    @Test
    void shouldThrowNotFoundWhenEmpty() {
        UUID id = UUID.randomUUID();
        when(partGateway.findById(id)).thenReturn(Optional.empty());

        FindPartByIdUseCase useCase = new FindPartByIdUseCaseImpl(partGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));

        verify(partGateway).findById(id);
        verifyNoMoreInteractions(partGateway);
    }
}
