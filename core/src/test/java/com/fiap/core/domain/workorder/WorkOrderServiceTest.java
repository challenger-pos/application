package com.fiap.core.domain.workorder;

import com.fiap.core.domain.service.Service;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WorkOrderServiceTest {

    @Test
    void minimalCtorShouldSetServiceIdAndQuantity() {
        UUID sid = UUID.randomUUID();
        WorkOrderService wos = new WorkOrderService(sid, 3);

        assertEquals(sid, wos.getServiceId());
        assertEquals(3, wos.getQuantity());
        assertNull(wos.getService());
        assertNull(wos.getAppliedPrice());
        assertNull(wos.getWorkOrder());
    }

    @Test
    void ctorWithDetailsShouldSetAllFields() {
        UUID sid = UUID.randomUUID();
        Service service = Service.builder()
                .id(UUID.randomUUID())
                .name("Alinhamento")
                .basePrice(new BigDecimal("150.00"))
                .build();
        BigDecimal applied = new BigDecimal("120.00");

        WorkOrderService wos = new WorkOrderService(sid, 2, service, applied, null);

        assertEquals(sid, wos.getServiceId());
        assertEquals(2, wos.getQuantity());
        assertSame(service, wos.getService());
        assertEquals(applied, wos.getAppliedPrice());
        assertNull(wos.getWorkOrder());
    }

    @Test
    void settersShouldUpdateFields() {
        WorkOrderService wos = new WorkOrderService(UUID.randomUUID(), 1);

        UUID newId = UUID.randomUUID();
        Service newService = Service.builder()
                .id(UUID.randomUUID())
                .name("Balanceamento")
                .basePrice(new BigDecimal("80.00"))
                .build();
        BigDecimal newPrice = new BigDecimal("75.50");

        wos.setServiceId(newId);
        wos.setService(newService);
        wos.setQuantity(4);
        wos.setAppliedPrice(newPrice);
        wos.setWorkOrder(null);

        assertEquals(newId, wos.getServiceId());
        assertSame(newService, wos.getService());
        assertEquals(4, wos.getQuantity());
        assertEquals(newPrice, wos.getAppliedPrice());
        assertNull(wos.getWorkOrder());
    }

    @Test
    void addQuantityShouldIncreaseQuantity() {
        WorkOrderService wos = new WorkOrderService(UUID.randomUUID(), 3);
        wos.addQuantity(2);
        assertEquals(5, wos.getQuantity());
    }
}
