package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.part.PartGateway;
import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.part.Part;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderPart;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.BadRequestException;
import com.fiap.core.exception.BusinessRuleException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import com.fiap.usecase.workorder.RefuseWorkOrderUseCase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RefuseWorkOrderUseCaseImpl implements RefuseWorkOrderUseCase {

    private final WorkOrderGateway workOrderGateway;
    private final PartGateway partGateway;

    public RefuseWorkOrderUseCaseImpl(WorkOrderGateway workOrderGateway, PartGateway partGateway) {
        this.workOrderGateway = workOrderGateway;
        this.partGateway = partGateway;
    }

    @Override
    public void execute(UUID id) throws NotFoundException, BadRequestException, BusinessRuleException {

        WorkOrder workOrder = workOrderGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeEnum.WORK0001.getMessage(), ErrorCodeEnum.WORK0001.getCode()));

        if (!WorkOrderStatus.AWAITING_APPROVAL.equals(workOrder.getStatus()))
            throw new BadRequestException(ErrorCodeEnum.WORK0006.getMessage(), ErrorCodeEnum.WORK0006.getCode());

        workOrder.restoreStock();
        workOrder.setStatus(WorkOrderStatus.COMPLETED);
        workOrder.setFinishedAt(LocalDateTime.now());

        List<Part> parts = workOrder.getWorkOrderParts().stream()
                .map(WorkOrderPart::getPart)
                .toList();

        partGateway.saveAll(parts);
        workOrderGateway.save(workOrder);
    }
}
