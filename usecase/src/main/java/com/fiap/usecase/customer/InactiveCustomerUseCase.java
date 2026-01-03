package com.fiap.usecase.customer;

import com.fiap.core.exception.DocumentNumberException;
import com.fiap.core.exception.NotFoundException;

import java.util.UUID;

public interface InactiveCustomerUseCase {

    void execute(UUID id) throws DocumentNumberException, NotFoundException;
}
