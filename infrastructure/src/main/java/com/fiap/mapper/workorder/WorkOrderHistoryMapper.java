package com.fiap.mapper.workorder;

import com.fiap.core.domain.workorder.WorkOrderHistory;
import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.dto.workorder.GetWorkOrderHistoryResponse;
import com.fiap.persistence.entity.workOrder.WorkOrderEntity;
import com.fiap.persistence.entity.workOrder.WorkOrderHistoryEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WorkOrderHistoryMapper {

    public WorkOrderHistory toDomain(WorkOrderHistoryEntity entity) {
        if (entity == null) return null;

        UUID workOrderId = entity.getWorkOrder() != null ? entity.getWorkOrder().getId() : null;

        return new WorkOrderHistory(
                workOrderId,
                null,
                entity.getStatus(),
                entity.getNotes(),
                entity.getCreatedAt()
        );
    }

    public List<WorkOrderHistory> toDomain(List<WorkOrderHistoryEntity> entities) {
        if (entities == null || entities.isEmpty()) return List.of();
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

    public List<WorkOrderHistory> fromWorkOrders(List<WorkOrderEntity> orders) {
        if (orders == null || orders.isEmpty()) return List.of();

        List<WorkOrderHistory> all = new ArrayList<>();

        for (WorkOrderEntity wo : orders) {

            if (wo.getCreatedAt() != null) {
                all.add(new WorkOrderHistory(
                        wo.getId(),
                        null,
                        WorkOrderStatus.RECEIVED,
                        null,
                        wo.getCreatedAt()
                ));
            }

            if (wo.getApprovedAt() != null) {
                all.add(new WorkOrderHistory(
                        wo.getId(),
                        null,
                        WorkOrderStatus.AWAITING_APPROVAL,
                        null,
                        wo.getApprovedAt()
                ));
            }

            if (wo.getFinishedAt() != null) {
                all.add(new WorkOrderHistory(
                        wo.getId(),
                        null,
                        WorkOrderStatus.COMPLETED,
                        null,
                        wo.getFinishedAt()
                ));
            }
        }

        all.sort(Comparator.comparing(WorkOrderHistory::getCreatedAt));
        return all;
    }

    public GetWorkOrderHistoryResponse toResponse(WorkOrderHistory h) {
        if (h == null) return null;
        return new GetWorkOrderHistoryResponse(
                h.getWorkOrderId(),
                h.getStatus() != null ? h.getStatus().getDescription() : null,
                h.getNotes(),
                h.getCreatedAt()
        );
    }

    public List<GetWorkOrderHistoryResponse> toResponse(List<WorkOrderHistory> histories) {
        if (histories == null || histories.isEmpty()) return List.of();
        return histories.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
