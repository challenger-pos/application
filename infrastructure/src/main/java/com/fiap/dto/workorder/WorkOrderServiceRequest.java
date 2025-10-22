package com.fiap.dto.workorder;

import java.util.UUID;

public record WorkOrderServiceRequest(
        UUID serviceId,
        Integer quantity
) {
}
