package com.fiap.usecase.workorder;

import com.fiap.core.domain.workorder.WorkOrderHistory;
import com.fiap.core.exception.NotFoundException;

import java.util.List;

public interface GetWorkOrderHistoryUseCase {

    List<WorkOrderHistory> execute(String cpfCnpj) throws NotFoundException;
}
