package com.fiap.mapper.workorder;

import com.fiap.core.domain.workorder.WorkOrderPart;
import com.fiap.dto.workorder.WorkOrderPartRequest;
import com.fiap.dto.workorder.WorkOrderPartResponse;
import com.fiap.mapper.part.PartMapper;
import com.fiap.persistence.entity.workOrder.WorkOrderPartEntity;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderPartMapper {

    private final PartMapper partMapper;

    public WorkOrderPartMapper(PartMapper partMapper) {
        this.partMapper = partMapper;
    }

    public WorkOrderPart toDomain(WorkOrderPartRequest request) {
        return new WorkOrderPart(
                request.partId(),
                request.quantity()
        );
    }

    public WorkOrderPart toDomain(WorkOrderPartEntity workOrderPartEntity) {
        return new WorkOrderPart(
                workOrderPartEntity.getPart().getId(),
                null,
                partMapper.toDomain(workOrderPartEntity.getPart()),
                workOrderPartEntity.getQuantity(),
                workOrderPartEntity.getUnitPrice()
        );
    }

    public WorkOrderPartEntity toEntity(WorkOrderPart workOrderPart) {
        return new WorkOrderPartEntity(
                null,
                null,
                partMapper.toEntity(workOrderPart.getPart()),
                workOrderPart.getQuantity(),
                workOrderPart.getAppliedPrice()
        );
    }

    public WorkOrderPartResponse toResponse(WorkOrderPart workOrderPart) {
        return new WorkOrderPartResponse(workOrderPart.getPart().getName(), workOrderPart.getQuantity());
    }
}
