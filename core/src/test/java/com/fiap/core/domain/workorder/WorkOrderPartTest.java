package com.fiap.core.domain.workorder;

import com.fiap.core.domain.part.Money;
import com.fiap.core.domain.part.Part;
import com.fiap.core.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WorkOrderPartTest {

    @Test
    void minimalCtorShouldSetPartIdAndQuantity() {
        UUID pid = UUID.randomUUID();
        WorkOrderPart wop = new WorkOrderPart(pid, 3);

        assertEquals(pid, wop.getPartId());
        assertEquals(3, wop.getQuantity());
        assertNull(wop.getWorkOrder());
        assertNull(wop.getPart());
        assertNull(wop.getAppliedPrice());
    }

    @Test
    void ctorWithMoneyShouldSetFields() throws BusinessRuleException {
        UUID pid = UUID.randomUUID();
        Part part = Part.builder().id(UUID.randomUUID()).name("Filtro").build();
        Money price = Money.of(new BigDecimal("12.34"));

        WorkOrderPart wop = new WorkOrderPart(pid, 2, part, price, null);

        assertEquals(pid, wop.getPartId());
        assertEquals(2, wop.getQuantity());
        assertSame(part, wop.getPart());
        assertNull(wop.getWorkOrder());
        assertEquals(new BigDecimal("12.34"), wop.getAppliedPrice());
    }

    @Test
    void ctorWithBigDecimalShouldSetFields() {
        UUID pid = UUID.randomUUID();
        Part part = Part.builder().id(UUID.randomUUID()).name("Óleo").build();
        BigDecimal price = new BigDecimal("99.90");

        WorkOrderPart wop = new WorkOrderPart(pid, null, part, 5, price);

        assertEquals(pid, wop.getPartId());
        assertEquals(5, wop.getQuantity());
        assertSame(part, wop.getPart());
        assertNull(wop.getWorkOrder());
        assertEquals(price, wop.getAppliedPrice());
    }

    @Test
    void settersShouldUpdateFields() {
        WorkOrderPart wop = new WorkOrderPart(UUID.randomUUID(), 1);

        UUID newId = UUID.randomUUID();
        Part newPart = Part.builder().id(UUID.randomUUID()).name("Correia").build();
        BigDecimal newPrice = new BigDecimal("7.50");

        wop.setPartId(newId);
        wop.setPart(newPart);
        wop.setQuantity(4);
        wop.setAppliedPrice(newPrice);
        wop.setWorkOrder(null);

        assertEquals(newId, wop.getPartId());
        assertSame(newPart, wop.getPart());
        assertEquals(4, wop.getQuantity());
        assertEquals(newPrice, wop.getAppliedPrice());
        assertNull(wop.getWorkOrder());
    }

    @Test
    void addQuantityShouldIncreaseQuantity() {
        WorkOrderPart wop = new WorkOrderPart(UUID.randomUUID(), 3);
        wop.addQuantity(2);
        assertEquals(5, wop.getQuantity());
    }
}
