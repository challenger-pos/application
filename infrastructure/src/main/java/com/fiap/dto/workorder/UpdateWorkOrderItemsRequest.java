package com.fiap.dto.workorder;

import java.util.List;

public record UpdateWorkOrderItemsRequest(
        List<WorkOrderPartRequest> parts,
        List<WorkOrderServiceRequest> services
) {

}
