package com.fiap.usecase.workorder;

import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.ForbiddenException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.UnauthorizedException;

import java.util.UUID;

public interface GetWorkOrderStatusUseCase {
    WorkOrderStatus execute(UUID id, String documentNumber) throws NotFoundException, UnauthorizedException, ForbiddenException;
}
