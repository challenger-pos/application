package com.fiap.application.usecaseimpl.vehicle;

import com.fiap.application.gateway.customer.CustomerGateway;
import com.fiap.application.gateway.vehicle.VehicleGateway;
import com.fiap.core.domain.customer.Customer;
import com.fiap.core.domain.vehicle.Vehicle;
import com.fiap.core.exception.DocumentNumberException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.vehicle.FindVehicleByIdUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateVehicleUseCaseImplTest {

    VehicleGateway vehicleGateway = mock(VehicleGateway.class);
    CustomerGateway customerGateway = mock(CustomerGateway.class);
    FindVehicleByIdUseCase findVehicleByIdUseCase = mock(FindVehicleByIdUseCase.class);

    Vehicle inputVehicle = mock(Vehicle.class);
    Vehicle existingVehicle = mock(Vehicle.class);
    Vehicle updatedVehicle = mock(Vehicle.class);

    Customer inputCustomer = mock(Customer.class);
    Customer foundCustomer = mock(Customer.class);

    @Test
    void shouldUpdateWhenVehicleAndCustomerExist() throws NotFoundException, DocumentNumberException {
        UUID vid = UUID.randomUUID();
        UUID cid = UUID.randomUUID();

        when(inputVehicle.getId()).thenReturn(vid);
        when(inputVehicle.getCustomer()).thenReturn(inputCustomer);
        when(inputCustomer.getId()).thenReturn(cid);

        when(findVehicleByIdUseCase.execute(vid)).thenReturn(existingVehicle);
        when(customerGateway.findById(cid)).thenReturn(Optional.of(foundCustomer));
        when(vehicleGateway.update(existingVehicle)).thenReturn(updatedVehicle);

        UpdateVehicleUseCaseImpl useCase =
                new UpdateVehicleUseCaseImpl(vehicleGateway, customerGateway, findVehicleByIdUseCase);

        when(inputVehicle.getLicensePlate()).thenReturn("ABC1D23");
        when(inputVehicle.getBrand()).thenReturn("BrandX");
        when(inputVehicle.getModel()).thenReturn("ModelY");
        when(inputVehicle.getYear()).thenReturn(2024);

        Vehicle result = useCase.execute(inputVehicle);

        assertSame(updatedVehicle, result);

        InOrder inOrder = inOrder(findVehicleByIdUseCase, customerGateway, existingVehicle, vehicleGateway);
        inOrder.verify(findVehicleByIdUseCase).execute(vid);
        inOrder.verify(customerGateway).findById(cid);
        inOrder.verify(existingVehicle).setCustomer(inputCustomer);
        inOrder.verify(existingVehicle).setLicensePlate("ABC1D23");
        inOrder.verify(existingVehicle).setBrand("BrandX");
        inOrder.verify(existingVehicle).setModel("ModelY");
        inOrder.verify(existingVehicle).setYear(2024);
        inOrder.verify(vehicleGateway).update(existingVehicle);
        verifyNoMoreInteractions(findVehicleByIdUseCase, customerGateway, existingVehicle, vehicleGateway);
    }

    @Test
    void shouldThrowNotFoundWhenVehicleDoesNotExist() throws NotFoundException {
        UUID vid = UUID.randomUUID();
        when(inputVehicle.getId()).thenReturn(vid);
        when(findVehicleByIdUseCase.execute(vid)).thenThrow(new NotFoundException("x","y"));

        UpdateVehicleUseCaseImpl useCase =
                new UpdateVehicleUseCaseImpl(vehicleGateway, customerGateway, findVehicleByIdUseCase);

        assertThrows(NotFoundException.class, () -> useCase.execute(inputVehicle));

        verify(findVehicleByIdUseCase).execute(vid);
        verifyNoInteractions(customerGateway, vehicleGateway, existingVehicle);
    }

    @Test
    void shouldThrowNotFoundWhenCustomerDoesNotExist() throws NotFoundException {
        UUID vid = UUID.randomUUID();
        UUID cid = UUID.randomUUID();

        when(inputVehicle.getId()).thenReturn(vid);
        when(inputVehicle.getCustomer()).thenReturn(inputCustomer);
        when(inputCustomer.getId()).thenReturn(cid);

        when(findVehicleByIdUseCase.execute(vid)).thenReturn(existingVehicle);
        when(customerGateway.findById(cid)).thenReturn(Optional.empty());

        UpdateVehicleUseCaseImpl useCase =
                new UpdateVehicleUseCaseImpl(vehicleGateway, customerGateway, findVehicleByIdUseCase);

        assertThrows(NotFoundException.class, () -> useCase.execute(inputVehicle));

        verify(findVehicleByIdUseCase).execute(vid);
        verify(customerGateway).findById(cid);
        verifyNoInteractions(vehicleGateway);
        verify(existingVehicle, never()).setCustomer(any());
    }
}
