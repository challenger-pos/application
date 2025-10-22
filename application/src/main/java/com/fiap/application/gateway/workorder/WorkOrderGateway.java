package com.fiap.application.gateway.workorder;

import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderHistory;
import com.fiap.core.domain.workorder.WorkOrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkOrderGateway {

    WorkOrder save(WorkOrder workOrder);
    Optional<WorkOrder> findById(UUID workOrderId);
    WorkOrder update(WorkOrder workOrder);
    List<WorkOrder> findByStatus(WorkOrderStatus workOrderStatus);
    List<WorkOrder> findAllOrdered(List<WorkOrderStatus> workOrderStatuses);
    List<WorkOrderHistory> getHistoryByCustomerCpfCnpj(String cpfCnpj);
}
