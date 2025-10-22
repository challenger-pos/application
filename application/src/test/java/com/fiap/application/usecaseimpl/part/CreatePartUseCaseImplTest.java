package com.fiap.application.usecaseimpl.part;

import com.fiap.application.gateway.part.PartGateway;
import com.fiap.core.domain.part.Part;
import com.fiap.usecase.part.CreatePartUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePartUseCaseImplTest {

    @Mock
    PartGateway partGateway;

    @Mock
    Part input;

    @Mock
    Part saved;

    @Test
    void shouldDelegateToGatewayAndReturnSaved() {
        when(partGateway.create(input)).thenReturn(saved);

        CreatePartUseCase useCase = new CreatePartUseCaseImpl(partGateway);
        Part result = useCase.execute(input);

        assertSame(saved, result);
        InOrder inOrder = inOrder(partGateway);
        inOrder.verify(partGateway).create(input);
        verifyNoMoreInteractions(partGateway);
    }

    @Test
    void shouldPassSameInstanceReceivedToGateway() {
        CreatePartUseCase useCase = new CreatePartUseCaseImpl(partGateway);
        when(partGateway.create(input)).thenReturn(saved);

        useCase.execute(input);

        verify(partGateway, times(1)).create(input);
        verifyNoMoreInteractions(partGateway);
    }
}
