package com.fiap.usecase.workorder;

import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.exception.BadRequestException;
import com.fiap.core.exception.BusinessRuleException;
import com.fiap.core.exception.NotFoundException;

public interface CreateWorkOrderUseCase {

    WorkOrder execute(WorkOrder workOrder) throws NotFoundException, BusinessRuleException, BadRequestException;
}
