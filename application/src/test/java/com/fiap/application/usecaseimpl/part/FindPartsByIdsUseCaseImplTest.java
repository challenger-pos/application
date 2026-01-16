package com.fiap.application.usecaseimpl.part;

import com.fiap.application.gateway.part.PartGateway;
import com.fiap.core.domain.part.Part;
import com.fiap.usecase.part.FindPartsByIdsUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindPartsByIdsUseCaseImplTest {

    @Mock
    PartGateway partGateway;

    @Mock
    Part part1;

    @Mock
    Part part2;

    @Test
    void shouldDelegateToGatewayAndReturnParts() {
        List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());
        List<Part> expected = List.of(part1, part2);

        when(partGateway.findByIds(ids)).thenReturn(expected);

        FindPartsByIdsUseCase useCase = new FindPartsByIdsUseCaseImpl(partGateway);
        List<Part> result = useCase.execute(ids);

        assertSame(expected, result);

        InOrder inOrder = inOrder(partGateway);
        inOrder.verify(partGateway).findByIds(ids);
        verifyNoMoreInteractions(partGateway);
    }
}
