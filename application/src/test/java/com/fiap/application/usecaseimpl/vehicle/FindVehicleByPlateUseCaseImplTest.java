package com.fiap.application.usecaseimpl.vehicle;

import com.fiap.application.gateway.vehicle.VehicleGateway;
import com.fiap.core.domain.vehicle.Vehicle;
import com.fiap.core.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindVehicleByPlateUseCaseImplTest {

    VehicleGateway vehicleGateway = mock(VehicleGateway.class);
    Vehicle vehicle = mock(Vehicle.class);

    @Test
    void shouldReturnVehicleWhenPlateExists() throws NotFoundException {
        String plate = "ABC1234";
        when(vehicleGateway.findByPlate(plate)).thenReturn(Optional.of(vehicle));

        FindVehicleByPlateUseCaseImpl useCase = new FindVehicleByPlateUseCaseImpl(vehicleGateway);
        Vehicle result = useCase.execute(plate);

        assertSame(vehicle, result);
        verify(vehicleGateway).findByPlate(plate);
        verifyNoMoreInteractions(vehicleGateway);
    }

    @Test
    void shouldThrowNotFoundWhenPlateDoesNotExist() {
        String plate = "ZZZ9999";
        when(vehicleGateway.findByPlate(plate)).thenReturn(Optional.empty());

        FindVehicleByPlateUseCaseImpl useCase = new FindVehicleByPlateUseCaseImpl(vehicleGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(plate));

        verify(vehicleGateway).findByPlate(plate);
        verifyNoMoreInteractions(vehicleGateway);
    }
}
