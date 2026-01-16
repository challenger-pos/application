package com.fiap.usecase.workorder;

import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.exception.BadRequestException;
import com.fiap.core.exception.NotFoundException;

import java.util.UUID;

public interface UpdateStatusWorkOrderUseCase {
    WorkOrder execute(UUID id, String newStatus)
        throws NotFoundException, BadRequestException;
}
