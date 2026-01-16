package com.fiap.application.usecaseimpl.vehicle;

import com.fiap.application.gateway.vehicle.VehicleGateway;
import com.fiap.core.domain.vehicle.Vehicle;
import com.fiap.core.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindVehicleByIdUseCaseImplTest {

    VehicleGateway vehicleGateway = mock(VehicleGateway.class);
    Vehicle vehicle = mock(Vehicle.class);

    @Test
    void shouldReturnVehicleWhenFound() throws NotFoundException {
        UUID id = UUID.randomUUID();
        when(vehicleGateway.findById(id)).thenReturn(Optional.of(vehicle));

        FindVehicleByIdUseCaseImpl useCase = new FindVehicleByIdUseCaseImpl(vehicleGateway);
        Vehicle result = useCase.execute(id);

        assertSame(vehicle, result);
        verify(vehicleGateway).findById(id);
        verifyNoMoreInteractions(vehicleGateway);
    }

    @Test
    void shouldThrowNotFoundWhenVehicleDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(vehicleGateway.findById(id)).thenReturn(Optional.empty());

        FindVehicleByIdUseCaseImpl useCase = new FindVehicleByIdUseCaseImpl(vehicleGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));

        verify(vehicleGateway).findById(id);
        verifyNoMoreInteractions(vehicleGateway);
    }
}
