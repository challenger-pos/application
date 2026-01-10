package com.fiap.controller;

import com.fiap.core.domain.vehicle.Vehicle;
import com.fiap.core.exception.DocumentNumberException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.dto.vehicle.CreateVehicleRequest;
import com.fiap.dto.vehicle.UpdateVehicleRequest;
import com.fiap.dto.vehicle.VehicleResponse;
import com.fiap.mapper.vehicle.VehicleMapper;
import com.fiap.usecase.vehicle.*;
import datadog.trace.api.Trace;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/v1/vehicles")
public class VehicleController {

    private final CreateVehicleUseCase createVehicleUseCase;
    private final FindVehicleByIdUseCase findVehicleByIdUseCase;
    private final FindVehicleByPlateUseCase findVehicleByPlateUseCase;
    private final UpdateVehicleUseCase updateVehicleUseCase;
    private final DeleteVehicleUseCase deleteVehicleUseCase;
    private final VehicleMapper vehicleMapper;

    public VehicleController(CreateVehicleUseCase createVehicleUseCase, FindVehicleByIdUseCase findVehicleByIdUseCase, FindVehicleByPlateUseCase findVehicleByPlateUseCase, UpdateVehicleUseCase updateVehicleUseCase, DeleteVehicleUseCase deleteVehicleUseCase, VehicleMapper vehicleMapper) {
        this.createVehicleUseCase = createVehicleUseCase;
        this.findVehicleByIdUseCase = findVehicleByIdUseCase;
        this.findVehicleByPlateUseCase = findVehicleByPlateUseCase;
        this.updateVehicleUseCase = updateVehicleUseCase;
        this.deleteVehicleUseCase = deleteVehicleUseCase;
        this.vehicleMapper = vehicleMapper;
    }

    @Operation(summary = "Cria um novo veículo")
    @PostMapping
    public ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody CreateVehicleRequest request) throws NotFoundException, DocumentNumberException {
        Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("operation.type", "create");
            if (request.licensePlate() != null) {
                span.setTag("vehicle.plate", request.licensePlate());
            }
            if (request.customerId() != null) {
                span.setTag("vehicle.customer_id", request.customerId().toString());
            }
        }
        Vehicle newVehicle = createVehicleUseCase.execute(vehicleMapper.toDomain(request));
        if (span != null && newVehicle != null) {
            span.setTag("vehicle.id", newVehicle.getId().toString());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleMapper.toResponse(newVehicle));
    }

    @Operation(summary = "Busca um veículo pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> findVehicleById(@PathVariable UUID id) throws NotFoundException {
        Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("operation.type", "findById");
            span.setTag("vehicle.id", id.toString());
        }
        Vehicle vehicle = findVehicleByIdUseCase.execute(id);
        if (span != null && vehicle != null) {
            span.setTag("vehicle.plate", vehicle.getLicensePlate());
        }
        return ResponseEntity.ok(vehicleMapper.toResponse(vehicle));
    }

    @Operation(summary = "Busca um veículo pela placa")
    @GetMapping("/by-plate")
    public ResponseEntity<VehicleResponse> findVehicleByPlate(@RequestParam String plate) throws NotFoundException {
        Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("operation.type", "findByPlate");
            span.setTag("vehicle.plate", plate);
        }
        Vehicle vehicle = findVehicleByPlateUseCase.execute(plate);
        if (span != null && vehicle != null) {
            span.setTag("vehicle.id", vehicle.getId().toString());
        }
        return ResponseEntity.ok(vehicleMapper.toResponse(vehicle));
    }

    @Operation(summary = "Atualiza um veículo")
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> updateVehicle(@PathVariable UUID id, @Valid @RequestBody UpdateVehicleRequest request) throws NotFoundException, DocumentNumberException {
        Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("operation.type", "update");
            span.setTag("vehicle.id", id.toString());
        }
        Vehicle updatedVehicle = updateVehicleUseCase.execute(vehicleMapper.toDomain(id, request));
        return ResponseEntity.ok(vehicleMapper.toResponse(updatedVehicle));
    }

    @Operation(summary = "Deleta um veículo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable UUID id) throws NotFoundException {
        Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("operation.type", "delete");
            span.setTag("vehicle.id", id.toString());
        }
        deleteVehicleUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}