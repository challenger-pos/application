package com.fiap.usecase.workorder;

import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.NotFoundException;

import java.util.UUID;

public interface GetWorkOrderStatusUseCase {
    WorkOrderStatus execute(UUID id) throws NotFoundException;
}
