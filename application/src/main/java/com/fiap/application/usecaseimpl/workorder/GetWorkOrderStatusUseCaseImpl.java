package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import com.fiap.usecase.workorder.GetWorkOrderStatusUseCase;

import java.util.UUID;

public class GetWorkOrderStatusUseCaseImpl implements GetWorkOrderStatusUseCase {

    private final WorkOrderGateway workOrderGateway;

    public GetWorkOrderStatusUseCaseImpl(WorkOrderGateway workOrderGateway) {
        this.workOrderGateway = workOrderGateway;
    }

    @Override
    public WorkOrderStatus execute(UUID id) throws NotFoundException {
        return workOrderGateway.findById(id)
                .map(WorkOrder::getStatus)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCodeEnum.WORK0001.getMessage(),
                        ErrorCodeEnum.WORK0001.getCode()
                ));
    }
}
