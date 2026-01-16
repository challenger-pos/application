package com.fiap.application.usecaseimpl.vehicle;

import com.fiap.application.gateway.vehicle.VehicleGateway;
import com.fiap.core.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteVehicleUseCaseImplTest {

    VehicleGateway vehicleGateway = mock(VehicleGateway.class);

    @Test
    void shouldDeleteWhenVehicleExists() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(vehicleGateway.existsById(id)).thenReturn(true);

        DeleteVehicleUseCaseImpl useCase = new DeleteVehicleUseCaseImpl(vehicleGateway);
        useCase.execute(id);

        verify(vehicleGateway).existsById(id);
        verify(vehicleGateway).delete(id);
        verifyNoMoreInteractions(vehicleGateway);
    }

    @Test
    void shouldThrowNotFoundWhenVehicleDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(vehicleGateway.existsById(id)).thenReturn(false);

        DeleteVehicleUseCaseImpl useCase = new DeleteVehicleUseCaseImpl(vehicleGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));

        verify(vehicleGateway).existsById(id);
        verifyNoMoreInteractions(vehicleGateway);
    }
}
