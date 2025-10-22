package com.fiap.application.usecaseimpl.workorder;

import com.fiap.application.gateway.workorder.WorkOrderGateway;

import com.fiap.core.domain.workorder.WorkOrder;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.usecase.workorder.CalculateAverageTimeWorkOrderUseCase;

import java.time.Duration;
import java.util.List;

public class CalculateAverageTimeWorkOrderUseCaseImpl implements CalculateAverageTimeWorkOrderUseCase {

    private final WorkOrderGateway workOrderGateway;

    public CalculateAverageTimeWorkOrderUseCaseImpl(WorkOrderGateway workOrderGateway) {
        this.workOrderGateway = workOrderGateway;
    }

    @Override
    public String execute() {
        List<WorkOrder> finishedOrders = workOrderGateway.findByStatus(WorkOrderStatus.COMPLETED);

        List<Duration> durations = finishedOrders.stream()
                .filter(order -> order.getFinishedAt() != null)
                .map(order -> {
                    var start = order.getApprovedAt() != null
                            ? order.getApprovedAt()
                            : order.getCreatedAt();

                    return Duration.between(start, order.getFinishedAt());
                })
                .toList();

        if (durations.isEmpty()) {
            return "00h 00m";
        }

        long totalMinutes = durations.stream()
                .mapToLong(Duration::toMinutes)
                .sum();

        double avgMinutes = (double) totalMinutes / durations.size();
        long roundedMinutes = Math.round(avgMinutes);

        long hours = roundedMinutes / 60;
        long minutes = roundedMinutes % 60;

        return String.format("%02dh %02dm", hours, minutes);
    }
}
