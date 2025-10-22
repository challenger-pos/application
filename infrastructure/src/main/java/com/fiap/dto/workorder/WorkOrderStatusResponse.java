package com.fiap.dto.workorder;

import java.util.UUID;

public record WorkOrderStatusResponse(
        UUID id,
        String status
) {
}
