package com.fiap.usecase.workorder;

import com.fiap.core.exception.*;

import java.util.UUID;

public interface RefuseWorkOrderUseCase {

    public void execute(UUID id, String documentNumber) throws NotFoundException, BadRequestException, BusinessRuleException, UnauthorizedException, ForbiddenException;
}
