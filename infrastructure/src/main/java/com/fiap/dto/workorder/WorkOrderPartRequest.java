package com.fiap.dto.workorder;

import java.util.UUID;

public record WorkOrderPartRequest(
        UUID partId,
        Integer quantity
) {
}
