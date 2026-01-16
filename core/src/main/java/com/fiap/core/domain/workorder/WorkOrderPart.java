package com.fiap.core.domain.workorder;

import com.fiap.core.domain.part.Money;
import com.fiap.core.domain.part.Part;

import java.math.BigDecimal;
import java.util.UUID;

public class WorkOrderPart {
    private UUID partId;
    private WorkOrder workOrder;
    private Part part;
    private Integer quantity;
    private BigDecimal appliedPrice;

    public WorkOrderPart(UUID partId, Integer quantity) {
        this.partId = partId;
        this.quantity = quantity;
    }

    public WorkOrderPart(UUID partId, Integer quantity, Part part, Money price, WorkOrder workOrder) {
        this.partId = partId;
        this.quantity = quantity;
        this.part = part;
        this.appliedPrice = price.getValue();
        this.workOrder = workOrder;
    }

    public WorkOrderPart(UUID partId, WorkOrder workOrder, Part part, Integer quantity, BigDecimal price) {
        this.partId = partId;
        this.quantity = quantity;
        this.part = part;
        this.appliedPrice = price;
        this.workOrder = workOrder;
    }

    public UUID getPartId() {
        return partId;
    }

    public void setPartId(UUID partId) {
        this.partId = partId;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAppliedPrice() {
        return appliedPrice;
    }

    public void setAppliedPrice(BigDecimal appliedPrice) {
        this.appliedPrice = appliedPrice;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }
}
