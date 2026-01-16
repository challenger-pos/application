package com.fiap.mapper.workorder;

import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderPart;
import com.fiap.core.domain.workorder.WorkOrderService;
import com.fiap.core.exception.BadRequestException;
import com.fiap.dto.workorder.*;
import com.fiap.mapper.customer.CustomerMapper;
import com.fiap.mapper.user.UserMapper;
import com.fiap.mapper.vehicle.VehicleMapper;
import com.fiap.persistence.entity.user.UserEntity;
import com.fiap.persistence.entity.workOrder.WorkOrderEntity;
import com.fiap.persistence.entity.workOrder.WorkOrderPartEntity;
import com.fiap.persistence.entity.workOrder.WorkOrderServiceEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkOrderMapper {

    private final CustomerMapper customerMapper;
    private final VehicleMapper vehicleMapper;
    private final UserMapper userMapper;
    private final WorkOrderServiceMapper workOrderServiceMapper;
    private final WorkOrderPartMapper workOrderPartMapper;

    public WorkOrderMapper(CustomerMapper customerMapper, VehicleMapper vehicleMapper, UserMapper userMapper, WorkOrderServiceMapper workOrderServiceMapper, WorkOrderPartMapper workOrderPartMapper) {
        this.customerMapper = customerMapper;
        this.vehicleMapper = vehicleMapper;
        this.userMapper = userMapper;
        this.workOrderServiceMapper = workOrderServiceMapper;
        this.workOrderPartMapper = workOrderPartMapper;
    }

    public WorkOrderEntity toEntity(WorkOrder workOrder) {
        List<WorkOrderPartEntity> workOrderPartEntities = workOrder.getWorkOrderParts().stream()
                .map(workOrderPartMapper::toEntity)
                .collect(Collectors.toList());

        List<WorkOrderServiceEntity> workOrderServiceEntities = workOrder.getWorkOrderServices().stream()
                .map(workOrderServiceMapper::toEntity)
                .collect(Collectors.toList());

        UserEntity mechanic = workOrder.getAssignedMechanic() != null ? userMapper.toEntity(workOrder.getAssignedMechanic()) : null;

        WorkOrderEntity workOrderEntity = new WorkOrderEntity(
                workOrder.getId(),
                customerMapper.toEntity(workOrder.getCustomer()),
                vehicleMapper.toEntity(workOrder.getVehicle()),
                userMapper.toEntity(workOrder.getCreatedBy()),
                mechanic,
                workOrderPartEntities,
                workOrderServiceEntities,
                workOrder.getStatus(),
                workOrder.getTotalAmount(),
                workOrder.getFinishedAt(),
                workOrder.getCreatedAt(),
                workOrder.getUpdatedAt(),
                workOrder.getApprovedAt()
        );

        workOrderEntity.getWorkOrderPartEntities()
                .forEach(part -> part.setWorkOrder(workOrderEntity));
        workOrderEntity.getWorkOrderServiceEntities()
                .forEach(service -> service.setWorkOrder(workOrderEntity));

        return workOrderEntity;
    }

    public WorkOrder toDomain(WorkOrderEntity workOrderEntity) {
        List<WorkOrderPart> workOrderParts = workOrderEntity.getWorkOrderPartEntities().stream()
                .map(workOrderPartMapper::toDomain)
                .collect(Collectors.toList());

        List<WorkOrderService> workOrderServices = workOrderEntity.getWorkOrderServiceEntities().stream()
                .map(workOrderServiceMapper::toDomain)
                .collect(Collectors.toList());

        return new WorkOrder(
                workOrderEntity.getId(),
                customerMapper.toDomain(workOrderEntity.getCustomer()),
                vehicleMapper.toDomain(workOrderEntity.getVehicle()),
                userMapper.toDomain(workOrderEntity.getCreatedBy()),
                workOrderParts,
                workOrderServices,
                workOrderEntity.getStatus(),
                workOrderEntity.getTotalAmount(),
                workOrderEntity.getCreatedAt(),
                workOrderEntity.getApprovedAt(),
                workOrderEntity.getFinishedAt()
        );
    }

    public WorkOrder toDomain(CreateWorkOrderRequest request) throws BadRequestException {
        List<WorkOrderService> services = request.services() != null && !request.services().isEmpty() ? request.services()
                .stream().map(workOrderServiceMapper::toDomain)
                .collect(Collectors.toList()) : new ArrayList<>();

        List<WorkOrderPart> parts = request.parts() != null && !request.parts().isEmpty() ? request.parts()
                .stream().map(workOrderPartMapper::toDomain)
                .collect(Collectors.toList()) : new ArrayList<>();

        return new WorkOrder(
                request.customerId(),
                request.vehicleId(),
                request.createdById(),
                parts,
                services
        );
    }

    public WorkOrder toDomain(UpdateWorkOrderItemsRequest request) throws BadRequestException {
        List<WorkOrderService> services = request.services() != null && !request.services().isEmpty() ? request.services()
                .stream().map(workOrderServiceMapper::toDomain)
                .collect(Collectors.toList()) : new ArrayList<>();

        List<WorkOrderPart> parts = request.parts() != null && !request.parts().isEmpty() ? request.parts()
                .stream().map(workOrderPartMapper::toDomain)
                .collect(Collectors.toList()) : new ArrayList<>();

        return new WorkOrder(
                parts,
                services
        );
    }

    public WorkOrderResponse toResponse(WorkOrder workOrder) {
        List<WorkOrderServiceResponse> services = workOrder.getWorkOrderServices() != null && !workOrder.getWorkOrderServices().isEmpty()
                        ? workOrder.getWorkOrderServices().stream().map(workOrderServiceMapper::toResponse).collect(Collectors.toList())
                        : new ArrayList<>();

        List<WorkOrderPartResponse> parts = workOrder.getWorkOrderParts() != null && !workOrder.getWorkOrderParts().isEmpty()
                        ? workOrder.getWorkOrderParts().stream().map(workOrderPartMapper::toResponse).collect(Collectors.toList())
                        : new ArrayList<>();

        return new WorkOrderResponse(
                workOrder.getId(),
                workOrder.getCustomer().getId(),
                workOrder.getVehicle().getId(),
                workOrder.getCreatedBy().getId(),
                workOrder.getTotalAmount(),
                parts,
                services,
                workOrder.getStatus() != null ? workOrder.getStatus().getDescription() : null,
                workOrder.getCreatedAt()
        );
    }
}
