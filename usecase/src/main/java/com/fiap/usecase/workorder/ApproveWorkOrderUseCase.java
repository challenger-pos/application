package com.fiap.usecase.workorder;

import com.fiap.core.exception.BadRequestException;
import com.fiap.core.exception.ForbiddenException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.UnauthorizedException;

import java.util.UUID;

public interface ApproveWorkOrderUseCase {

    public void execute(UUID id, String documentNumber) throws NotFoundException, BadRequestException, UnauthorizedException, ForbiddenException;
}
