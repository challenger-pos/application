package com.fiap.usecase.workorder;

import com.fiap.core.exception.BadRequestException;
import com.fiap.core.exception.NotFoundException;

import java.util.UUID;

public interface AssignedMechanicUseCase {

    public void execute (UUID workOrderId, UUID mechanicId) throws NotFoundException, BadRequestException;
}
