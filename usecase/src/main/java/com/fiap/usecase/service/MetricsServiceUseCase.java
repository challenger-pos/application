package com.fiap.usecase.service;

public interface MetricsServiceUseCase {
    void execute(long durationInSeconds, String statusTag);
}