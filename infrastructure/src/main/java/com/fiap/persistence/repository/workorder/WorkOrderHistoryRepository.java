package com.fiap.persistence.repository.workorder;

import com.fiap.persistence.entity.workOrder.WorkOrderEntity;
import com.fiap.persistence.entity.workOrder.WorkOrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkOrderHistoryRepository extends JpaRepository<WorkOrderHistoryEntity, UUID> {
    List<WorkOrderHistoryEntity> findByWorkOrderOrderByCreatedAtDesc(WorkOrderEntity workOrder);
}
