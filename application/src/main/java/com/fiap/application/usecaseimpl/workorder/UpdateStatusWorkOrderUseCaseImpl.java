package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.BadRequestException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import com.fiap.usecase.workorder.UpdateStatusWorkOrderUseCase;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateStatusWorkOrderUseCaseImpl implements UpdateStatusWorkOrderUseCase {

    private final WorkOrderGateway workOrderGateway;

    public UpdateStatusWorkOrderUseCaseImpl(WorkOrderGateway workOrderGateway) {
        this.workOrderGateway = workOrderGateway;
    }

    @Override
    public WorkOrder execute(UUID id, String newStatus)
            throws NotFoundException, BadRequestException {

        WorkOrder workOrder = workOrderGateway.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                ErrorCodeEnum.WORK0001.getMessage(),
                                ErrorCodeEnum.WORK0001.getCode()
                        )
                );

        final WorkOrderStatus statusEnum;
        try {
            statusEnum = WorkOrderStatus.fromString(newStatus);
        } catch (Exception ex) {
            throw new BadRequestException(
                    ErrorCodeEnum.WORK0004.getMessage(),
                    ErrorCodeEnum.WORK0004.getCode()
            );
        }

        if (workOrder.getStatus() == statusEnum) throw new BadRequestException(ErrorCodeEnum.WORK0005.getMessage(), ErrorCodeEnum.WORK0005.getCode());

        if (statusEnum == WorkOrderStatus.DELIVERED || statusEnum == WorkOrderStatus.COMPLETED) {
            workOrder.setFinishedAt(LocalDateTime.now());
        }

        workOrder.setStatus(statusEnum);
        workOrder.setUpdatedAt(LocalDateTime.now());

        return workOrderGateway.update(workOrder);
    }
}
