package com.fiap.usecase.workorder;

import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.exception.NotFoundException;

import java.util.UUID;

public interface FindWorkOrderByIdUseCase {

    WorkOrder execute(UUID id) throws NotFoundException;
}
