package com.fiap.persistence.repository.workorder;

import com.fiap.persistence.entity.workOrder.WorkOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkOrderHistoryRepository extends JpaRepository<WorkOrderEntity, UUID> {

    @Query("""
        select w
        from WorkOrderEntity w
          join w.customer c
        where c.documentNumber = :documentNumber
        order by w.createdAt asc
    """)
    List<WorkOrderEntity> findByCustomerDocumentNumberOrderByCreatedAtAsc(
            @Param("documentNumber") String documentNumber
    );
}
