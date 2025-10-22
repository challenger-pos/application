package com.fiap.core.domain.workorder;

import com.fiap.core.domain.service.Service;

import java.math.BigDecimal;
import java.util.UUID;

public class WorkOrderService {
    private UUID serviceId;
    private WorkOrder workOrder;
    private Service service;
    private Integer quantity;
    private BigDecimal appliedPrice;

    public WorkOrderService(UUID serviceId, Integer quantity) {
        this.serviceId = serviceId;
        this.quantity = quantity;
    }

    public WorkOrderService(UUID serviceId, Integer quantity, Service service, BigDecimal appliedPrice, WorkOrder workOrder) {
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.service = service;
        this.appliedPrice = appliedPrice;
        this.workOrder = workOrder;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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
