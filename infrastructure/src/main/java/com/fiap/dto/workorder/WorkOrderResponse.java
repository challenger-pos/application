package com.fiap.dto.workorder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record WorkOrderResponse(
        UUID id,
        UUID customerId,
        UUID vehicleId,
        UUID createdById,
        BigDecimal totalAmount,
        List<WorkOrderPartResponse> parts,
        List<WorkOrderServiceResponse> services,
        String status,
        LocalDateTime createdAt
) {
}
