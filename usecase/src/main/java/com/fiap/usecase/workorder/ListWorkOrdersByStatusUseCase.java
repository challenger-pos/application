package com.fiap.usecase.workorder;

import com.fiap.core.domain.workorder.WorkOrder;

import java.util.List;

public interface ListWorkOrdersByStatusUseCase {

    public List<WorkOrder> execute();
}
