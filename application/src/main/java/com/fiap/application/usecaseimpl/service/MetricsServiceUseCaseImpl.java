package com.fiap.application.usecaseimpl.service;

import com.fiap.application.gateway.service.ServiceGateway;
import com.fiap.core.domain.service.Service;
import com.fiap.core.exception.NotFoundException;
import com.fiap.usecase.service.FindServiceByIdUseCase;
import com.fiap.usecase.service.MetricsServiceUseCase;
import com.fiap.usecase.service.UpdateServiceUseCase;

public class MetricsServiceUseCaseImpl implements MetricsServiceUseCase {

    private final ServiceGateway serviceGateway;

    public MetricsServiceUseCaseImpl(ServiceGateway serviceGateway) {
        this.serviceGateway = serviceGateway;
    }

    @Override
    public void execute(long durationInSeconds, String statusTag) {
        serviceGateway.sendWorkOrderStatusTransitionDuration(durationInSeconds, statusTag);
    }
}