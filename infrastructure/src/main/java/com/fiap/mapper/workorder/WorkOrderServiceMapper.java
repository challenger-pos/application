package com.fiap.mapper.workorder;

import com.fiap.core.domain.workorder.WorkOrderService;
import com.fiap.dto.workorder.WorkOrderServiceRequest;
import com.fiap.dto.workorder.WorkOrderServiceResponse;
import com.fiap.mapper.service.ServiceMapper;
import com.fiap.persistence.entity.workOrder.WorkOrderServiceEntity;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderServiceMapper {

    private final ServiceMapper serviceMapper;

    public WorkOrderServiceMapper(ServiceMapper serviceMapper) {
        this.serviceMapper = serviceMapper;
    }

    public WorkOrderService toDomain(WorkOrderServiceRequest request) {
        return new WorkOrderService(
                request.serviceId(),
                request.quantity()
        );
    }

    public WorkOrderService toDomain(WorkOrderServiceEntity workOrderService) {
        return new WorkOrderService(
                workOrderService.getService().getId(),
                workOrderService.getQuantity(),
                serviceMapper.toDomain(workOrderService.getService()),
                workOrderService.getAppliedPrice(),
                null
        );
    }

    public WorkOrderServiceEntity toEntity(WorkOrderService workOrderService) {
        return new WorkOrderServiceEntity(
                null,
                null,
                serviceMapper.toEntity(workOrderService.getService()),
                workOrderService.getQuantity(),
                workOrderService.getAppliedPrice()
        );
    }

    public WorkOrderServiceResponse toResponse(WorkOrderService workOrderService) {
        return new WorkOrderServiceResponse(workOrderService.getService().getName(), workOrderService.getQuantity());
    }
}
