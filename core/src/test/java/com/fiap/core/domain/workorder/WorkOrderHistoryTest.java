package com.fiap.core.domain.workorder;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WorkOrderHistoryTest {

    @Test
    void minimalCtorShouldSetWorkOrderIdAndStatus() {
        UUID id = UUID.randomUUID();
        WorkOrderStatus status = WorkOrderStatus.RECEIVED;

        WorkOrderHistory h = new WorkOrderHistory(id, status);

        assertEquals(id, h.getWorkOrderId());
        assertEquals(status, h.getStatus());
        assertNull(h.getWorkOrder());
        assertNull(h.getNotes());
        assertNull(h.getCreatedAt());
    }

    @Test
    void fullCtorShouldSetAllFields() {
        UUID id = UUID.randomUUID();
        WorkOrderStatus status = WorkOrderStatus.IN_PROGRESS;
        String notes = "Diagnóstico inicial";
        LocalDateTime created = LocalDateTime.now().minusHours(2);

        WorkOrderHistory h = new WorkOrderHistory(id, null, status, notes, created);

        assertEquals(id, h.getWorkOrderId());
        assertNull(h.getWorkOrder());
        assertEquals(status, h.getStatus());
        assertEquals(notes, h.getNotes());
        assertEquals(created, h.getCreatedAt());
    }

    @Test
    void settersShouldUpdateFields() {
        WorkOrderHistory h = new WorkOrderHistory(UUID.randomUUID(), WorkOrderStatus.RECEIVED);

        UUID newId = UUID.randomUUID();
        WorkOrderStatus newStatus = WorkOrderStatus.COMPLETED;
        String newNotes = "Finalizada";
        LocalDateTime newCreated = LocalDateTime.now();

        h.setWorkOrderId(newId);
        h.setWorkOrder(null);
        h.setStatus(newStatus);
        h.setNotes(newNotes);
        h.setCreatedAt(newCreated);

        assertEquals(newId, h.getWorkOrderId());
        assertNull(h.getWorkOrder());
        assertEquals(newStatus, h.getStatus());
        assertEquals(newNotes, h.getNotes());
        assertEquals(newCreated, h.getCreatedAt());
    }

    @Test
    void allowsNullOptionalFields() {
        WorkOrderHistory h = new WorkOrderHistory(UUID.randomUUID(), WorkOrderStatus.RECEIVED);
        h.setWorkOrder(null);
        h.setNotes(null);
        h.setCreatedAt(null);

        assertNull(h.getWorkOrder());
        assertNull(h.getNotes());
        assertNull(h.getCreatedAt());
    }
}
