package com.fiap.gateway.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderHistory;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.mapper.workorder.WorkOrderHistoryMapper;
import com.fiap.mapper.workorder.WorkOrderMapper;
import com.fiap.persistence.entity.workOrder.WorkOrderEntity;
import com.fiap.persistence.repository.workorder.WorkOrderRepository;
import com.fiap.persistence.repository.workorder.WorkOrderHistoryRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WorkOrderRepositoryGateway implements WorkOrderGateway {

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderHistoryRepository workOrderHistoryRepository;
    private final WorkOrderMapper workOrderMapper;
    private final WorkOrderHistoryMapper workOrderHistoryMapper;

    public WorkOrderRepositoryGateway(
            WorkOrderRepository workOrderRepository,
            WorkOrderHistoryRepository workOrderHistoryRepository,
            WorkOrderMapper workOrderMapper,
            WorkOrderHistoryMapper workOrderHistoryMapper
    ) {
        this.workOrderRepository = workOrderRepository;
        this.workOrderHistoryRepository = workOrderHistoryRepository;
        this.workOrderMapper = workOrderMapper;
        this.workOrderHistoryMapper = workOrderHistoryMapper;
    }

    @Transactional
    @Override
    public WorkOrder save(WorkOrder workOrder) {
        WorkOrderEntity workOrderEntity = workOrderRepository.save(workOrderMapper.toEntity(workOrder));
        return workOrderMapper.toDomain(workOrderEntity);
    }

    @Transactional
    @Override
    public Optional<WorkOrder> findById(UUID workOrderId) {
        Optional<WorkOrderEntity> workOrderEntity = workOrderRepository.findById(workOrderId);

        return workOrderEntity.map(workOrderMapper::toDomain);
    }

    @Transactional
    @Override
    public WorkOrder update(WorkOrder workOrder) {
        WorkOrderEntity entity = workOrderMapper.toEntity(workOrder);
        WorkOrderEntity saved = workOrderRepository.save(entity);
        return workOrderMapper.toDomain(saved);
    }

    @Transactional
    @Override
    public List<WorkOrder> findByStatus(WorkOrderStatus workOrderStatus) {
        List<WorkOrderEntity> workOrderEntities = workOrderRepository.findByStatus(workOrderStatus);
        return workOrderEntities.stream().map(workOrderMapper::toDomain).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<WorkOrder> findAllOrdered(List<WorkOrderStatus> workOrderStatuses) {
        List<WorkOrderEntity> workOrderEntities = workOrderRepository.findAllOrdered(workOrderStatuses);
        return workOrderEntities.stream().map(workOrderMapper::toDomain).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<WorkOrderHistory> getHistoryByCustomerCpfCnpj(String cpfCnpj) {
        String cleaned = cpfCnpj.replaceAll("\\D", "");
        var orders = workOrderHistoryRepository.findByCustomerDocumentNumberOrderByCreatedAtAsc(cleaned);
        return workOrderHistoryMapper.fromWorkOrders(orders);
    }
}
