package com.fiap.dto.workorder;

import java.util.List;
import java.util.UUID;

public record CreateWorkOrderRequest(
        UUID customerId,
        UUID vehicleId,
        UUID createdById,
        List<WorkOrderPartRequest> parts,
        List<WorkOrderServiceRequest> services
) {

}
