package com.fiap.application.usecaseimpl.vehicle;

import com.fiap.application.gateway.customer.CustomerGateway;
import com.fiap.application.gateway.vehicle.VehicleGateway;
import com.fiap.core.domain.customer.Customer;
import com.fiap.core.domain.vehicle.Vehicle;
import com.fiap.core.exception.DocumentNumberException;
import com.fiap.core.exception.NotFoundException;
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
class CreateVehicleUseCaseImplTest {

    @Mock VehicleGateway vehicleGateway;
    @Mock CustomerGateway customerGateway;

    @Mock Vehicle inputVehicle;
    @Mock Vehicle createdVehicle;
    @Mock Customer vehicleOwnerRef;
    @Mock Customer persistedCustomer;

    @Test
    void shouldCreateVehicleAndAttachCustomer() throws NotFoundException, DocumentNumberException {
        UUID customerId = UUID.randomUUID();

        when(inputVehicle.getCustomer()).thenReturn(vehicleOwnerRef);
        when(vehicleOwnerRef.getId()).thenReturn(customerId);
        when(customerGateway.findById(customerId)).thenReturn(Optional.of(persistedCustomer));
        when(vehicleGateway.create(inputVehicle)).thenReturn(createdVehicle);

        CreateVehicleUseCaseImpl useCase = new CreateVehicleUseCaseImpl(vehicleGateway, customerGateway);

        Vehicle result = useCase.execute(inputVehicle);

        assertSame(createdVehicle, result);
        verify(createdVehicle).setCustomer(persistedCustomer);

        InOrder inOrder = inOrder(customerGateway, vehicleGateway);
        inOrder.verify(customerGateway).findById(customerId);
        inOrder.verify(vehicleGateway).create(inputVehicle);
        verifyNoMoreInteractions(customerGateway, vehicleGateway);
    }

    @Test
    void shouldThrowNotFoundWhenCustomerDoesNotExist() throws DocumentNumberException {
        UUID customerId = UUID.randomUUID();

        when(inputVehicle.getCustomer()).thenReturn(vehicleOwnerRef);
        when(vehicleOwnerRef.getId()).thenReturn(customerId);
        doReturn(Optional.empty()).when(customerGateway).findById(customerId);

        CreateVehicleUseCaseImpl useCase = new CreateVehicleUseCaseImpl(vehicleGateway, customerGateway);

        assertThrows(NotFoundException.class, () -> useCase.execute(inputVehicle));

        verify(customerGateway).findById(customerId);
        verifyNoInteractions(vehicleGateway);
        verifyNoMoreInteractions(customerGateway);
    }
}
