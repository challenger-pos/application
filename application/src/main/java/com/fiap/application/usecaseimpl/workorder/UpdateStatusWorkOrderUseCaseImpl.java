package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.service.ServiceGateway;
import com.fiap.application.gateway.workorder.WorkOrderGateway;
import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderHistory;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.core.exception.BadRequestException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import com.fiap.usecase.workorder.UpdateStatusWorkOrderUseCase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class UpdateStatusWorkOrderUseCaseImpl implements UpdateStatusWorkOrderUseCase {

    private final WorkOrderGateway workOrderGateway;
    private final ServiceGateway serviceGateway;

    public UpdateStatusWorkOrderUseCaseImpl(WorkOrderGateway workOrderGateway, ServiceGateway serviceGateway) {
        this.workOrderGateway = workOrderGateway;
        this.serviceGateway = serviceGateway;
    }

    @Override
    public WorkOrder execute(UUID id, String newStatus)
            throws NotFoundException, BadRequestException {

        WorkOrder workOrder = workOrderGateway.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                ErrorCodeEnum.WORK0001.getMessage(),
                                ErrorCodeEnum.WORK0001.getCode()
                        )
                );

        final WorkOrderStatus statusEnum;
        try {
            statusEnum = WorkOrderStatus.fromString(newStatus);
        } catch (Exception ex) {
            throw new BadRequestException(
                    ErrorCodeEnum.WORK0004.getMessage(),
                    ErrorCodeEnum.WORK0004.getCode()
            );
        }

        if (workOrder.getStatus() == statusEnum) throw new BadRequestException(ErrorCodeEnum.WORK0005.getMessage(), ErrorCodeEnum.WORK0005.getCode());

        if (statusEnum == WorkOrderStatus.DELIVERED || statusEnum == WorkOrderStatus.COMPLETED) {
            workOrder.setFinishedAt(LocalDateTime.now());
        }

        workOrder.setStatus(statusEnum);
        workOrder.setUpdatedAt(LocalDateTime.now());

        // Calcular e enviar métrica de tempo médio por status
        try {
            List<WorkOrderHistory> historyList = workOrderGateway.findHistoryByWorkOrderIdOrderByCreatedAtDesc(id);
            if (historyList.size() >= 2) {
                WorkOrderHistory latestHistory = historyList.get(0);  // Status atual (mais recente)
                WorkOrderHistory previousHistory = historyList.get(1); // Status anterior

                if (latestHistory.getCreatedAt() != null && previousHistory.getCreatedAt() != null) {
                    Duration duration = Duration.between(previousHistory.getCreatedAt(), latestHistory.getCreatedAt());
                    long durationInSeconds = duration.getSeconds();

                    // Enviar métrica para Datadog
                    String statusTag = "status:" + latestHistory.getStatus().name();
                    serviceGateway.sendWorkOrderStatusTransitionDuration(durationInSeconds, statusTag);
                }
            }
        } catch (Exception e) {
            // Log do erro mas não afeta a resposta da API
            // Exceções já são tratadas dentro do MetricsService, mas mantemos aqui como segurança adicional
        }

        WorkOrder updatedWorkOrder = workOrderGateway.update(workOrder);

        // Salvar histórico com o novo status
        WorkOrderHistory history = new WorkOrderHistory(updatedWorkOrder.getId(), statusEnum);
        history.setCreatedAt(LocalDateTime.now());
        workOrderGateway.saveHistory(history);

        return updatedWorkOrder;
    }
}
