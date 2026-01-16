package com.fiap.dto.workorder;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetWorkOrderHistoryResponse(
        UUID workOrderId,
        String status,
        String notes,
        LocalDateTime createdAt
) { }
