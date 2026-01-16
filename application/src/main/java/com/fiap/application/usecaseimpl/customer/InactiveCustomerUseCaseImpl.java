package com.fiap.application.usecaseimpl.customer;

import com.fiap.application.gateway.customer.CustomerGateway;
import com.fiap.core.domain.customer.Customer;
import com.fiap.core.exception.DocumentNumberException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import com.fiap.usecase.customer.InactiveCustomerUseCase;

import java.util.UUID;

public class InactiveCustomerUseCaseImpl implements InactiveCustomerUseCase {

    private final CustomerGateway customerGateway;

    public InactiveCustomerUseCaseImpl(CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    @Override
    public void execute(UUID id) throws DocumentNumberException, NotFoundException {
        Customer customer = customerGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeEnum.CUST0001.getMessage(), ErrorCodeEnum.CUST0001.getCode()));

        if (customer.getIsActive()) {
            customer.setIsActive(false);
            customerGateway.update(customer);
        }

    }
}