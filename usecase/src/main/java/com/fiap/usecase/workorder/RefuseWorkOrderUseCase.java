package com.fiap.usecase.workorder;

import com.fiap.core.exception.BadRequestException;
import com.fiap.core.exception.BusinessRuleException;
import com.fiap.core.exception.NotFoundException;

import java.util.UUID;

public interface RefuseWorkOrderUseCase {

    public void execute(UUID id) throws NotFoundException, BadRequestException, BusinessRuleException;
}
