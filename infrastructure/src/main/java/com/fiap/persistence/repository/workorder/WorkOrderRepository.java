package com.fiap.persistence.repository.workorder;

import java.util.List;
import java.util.UUID;

import com.fiap.core.domain.workorder.WorkOrderStatus;
import com.fiap.persistence.entity.workOrder.WorkOrderEntity;
import com.fiap.persistence.entity.customer.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrderEntity, UUID>, JpaSpecificationExecutor<WorkOrderEntity> {

    List<WorkOrderEntity> findByStatus(WorkOrderStatus status);

    List<WorkOrderEntity> findByCustomerId(UUID customerId);

    List<WorkOrderEntity> findByAssignedMechanicId(UUID mechanicId);

	List<WorkOrderEntity> findByCustomerOrderByCreatedAtDesc(CustomerEntity customer);

    @Query(value = """
        SELECT w FROM WorkOrderEntity w
        WHERE w.status NOT IN (:excludedStatuses)
        ORDER BY
            CASE
                WHEN w.status = 'IN_PROGRESS' THEN 1
                WHEN w.status = 'AWAITING_APPROVAL' THEN 2
                WHEN w.status = 'IN_DIAGNOSIS' THEN 3
                WHEN w.status = 'RECEIVED' THEN 4
                ELSE 5
            END,
            w.createdAt ASC
    """)
    List<WorkOrderEntity> findAllOrdered(
            @Param("excludedStatuses") List<WorkOrderStatus> excludedStatuses
    );
}
