package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.usecase.workorder.ListWorkOrdersByStatusUseCase;

import java.util.List;

public class ListWorkOrdersByStatusUseCaseImpl implements ListWorkOrdersByStatusUseCase {

    private final WorkOrderGateway workOrderGateway;

    public ListWorkOrdersByStatusUseCaseImpl(WorkOrderGateway workOrderGateway) {
        this.workOrderGateway = workOrderGateway;
    }

    @Override
    public List<WorkOrder> execute() {
        return workOrderGateway.findAllOrdered(List.of(WorkOrderStatus.COMPLETED, WorkOrderStatus.DELIVERED, WorkOrderStatus.REFUSED));
    }
}
