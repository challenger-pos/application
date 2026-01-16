package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.ForbiddenException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.UnauthorizedException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import com.fiap.usecase.workorder.GetWorkOrderStatusUseCase;

import java.util.Objects;
import java.util.UUID;

public class GetWorkOrderStatusUseCaseImpl implements GetWorkOrderStatusUseCase {

    private final WorkOrderGateway workOrderGateway;

    public GetWorkOrderStatusUseCaseImpl(WorkOrderGateway workOrderGateway) {
        this.workOrderGateway = workOrderGateway;
    }

    @Override
    public WorkOrderStatus execute(UUID id, String documentNumber) throws NotFoundException, UnauthorizedException, ForbiddenException {
        if (documentNumber == null) {
            throw  new UnauthorizedException(ErrorCodeEnum.WORK0008.getMessage(), ErrorCodeEnum.WORK0008.getCode());
        }

        WorkOrder workOrder = workOrderGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeEnum.WORK0001.getMessage(), ErrorCodeEnum.WORK0001.getCode()));

        if (!Objects.equals(workOrder.getCustomer().getDocumentNumber().getValue(), documentNumber)) {
            throw new ForbiddenException(ErrorCodeEnum.WORK0007.getMessage(), ErrorCodeEnum.WORK0007.getCode());
        }

        return workOrder.getStatus();
    }
}
