package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrderHistory;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import com.fiap.usecase.workorder.GetWorkOrderHistoryUseCase;

import java.util.Comparator;
import java.util.List;

public class GetWorkOrderHistoryUseCaseImpl implements GetWorkOrderHistoryUseCase {

    private final WorkOrderGateway workOrderGateway;

    public GetWorkOrderHistoryUseCaseImpl(WorkOrderGateway workOrderGateway) {
        this.workOrderGateway = workOrderGateway;
    }

    @Override
    public List<WorkOrderHistory> execute(String cpfCnpj) throws NotFoundException {
        List<WorkOrderHistory> histories = workOrderGateway.getHistoryByCustomerCpfCnpj(cpfCnpj);

        if (histories == null || histories.isEmpty()) {
            throw new NotFoundException(
                    ErrorCodeEnum.HIST0001.getMessage(),
                    ErrorCodeEnum.HIST0001.getCode()
            );
        }

        histories.sort(Comparator.comparing(WorkOrderHistory::getCreatedAt));
        return histories;
    }
}
