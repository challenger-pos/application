package com.fiap.controller;

import com.fiap.core.domain.customer.Customer;
import com.fiap.core.exception.DocumentNumberException;
import com.fiap.core.exception.EmailException;
import com.fiap.core.exception.InternalServerErrorException;
import com.fiap.core.exception.NotFoundException;
import com.fiap.dto.customer.CreateCustomerRequest;
import com.fiap.dto.customer.CustomerResponse;
import com.fiap.dto.customer.UpdateCustomerRequest;
import com.fiap.mapper.customer.CustomerMapper;
import com.fiap.usecase.customer.*;
import datadog.trace.api.Trace;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("v1/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CreateCustomerUseCase createCustomerUseCase;
    private final FindCustomerByIdUseCase findCustomerByIdUseCase;
    private final FindCustomerByDocumentUseCase findCustomerByDocumentUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final ActiveCustomerUseCase activeCustomerUseCase;
    private final InactiveCustomerUseCase inactiveCustomerUseCase;
    private final CustomerMapper customerMapper;

    public CustomerController(CreateCustomerUseCase createCustomerUseCase, FindCustomerByIdUseCase findCustomerByIdUseCase, FindCustomerByDocumentUseCase findCustomerByDocumentUseCase, UpdateCustomerUseCase updateCustomerUseCase, DeleteCustomerUseCase deleteCustomerUseCase, ActiveCustomerUseCase activeCustomerUseCase, InactiveCustomerUseCase inactiveCustomerUseCase, CustomerMapper customerMapper, Tracer tracer) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.findCustomerByIdUseCase = findCustomerByIdUseCase;
        this.findCustomerByDocumentUseCase = findCustomerByDocumentUseCase;
        this.updateCustomerUseCase = updateCustomerUseCase;
        this.deleteCustomerUseCase = deleteCustomerUseCase;
        this.activeCustomerUseCase = activeCustomerUseCase;
        this.inactiveCustomerUseCase = inactiveCustomerUseCase;
        this.customerMapper = customerMapper;
    }

    @Operation(
            summary = "Cria um novo cliente",
            description = "Endpoint para criar um novo cliente.")
    @ApiResponses(
            value = { @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso.") })
    @PostMapping("/create")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CreateCustomerRequest request) throws DocumentNumberException, EmailException, InternalServerErrorException {
        Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("operation.type", "create");
            if (request.documentNumber() != null) {
                span.setTag("customer.document", request.documentNumber());
            }
        }
        try {
            MDC.put("operation.type", "create_customer");
            if (request.documentNumber() != null) {
                MDC.put("customer.document", request.documentNumber().toString());
            }
            logger.info("Creating customer with document: {}", request.documentNumber());

            Customer customer = createCustomerUseCase.execute(customerMapper.toDomain(request));
            if (span != null && customer != null) {
                span.setTag("customer.id", customer.getId().toString());
            }

            if (customer != null) {
                MDC.put("customer.id", customer.getId().toString());
                logger.info("Customer created successfully with id: {}", customer.getId());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(customerMapper.toResponse(customer));
        } finally {
            MDC.remove("operation.type");
            MDC.remove("customer.document");
            MDC.remove("customer.id");
        }
    }

    @Operation(
            summary = "Atualiza um cliente existente",
            description = "Endpoint para atualizar um cliente pelo ID.")
    @ApiResponses(
            value = { @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso.") })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID id, @RequestBody UpdateCustomerRequest request) throws DocumentNumberException, EmailException, NotFoundException, InternalServerErrorException {
        Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("operation.type", "update");
            span.setTag("customer.id", id.toString());
            if (request.documentNumber() != null) {
                span.setTag("customer.document", request.documentNumber());
            }
        }
        Customer customer = updateCustomerUseCase.execute(customerMapper.toDomain(id, request));
        return ResponseEntity.ok().body(customerMapper.toResponse(customer));
    }

    @Operation(
            summary = "Ativa um cliente existente",
            description = "Endpoint para ativar um cliente pelo ID.")
    @ApiResponses(
            value = { @ApiResponse(responseCode = "200", description = "Cliente ativado com sucesso.") })
    @PutMapping("/active/{id}")
    public ResponseEntity<Void> activeCustomer(@PathVariable UUID id) throws DocumentNumberException, NotFoundException {
        activeCustomerUseCase.execute(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Inativa um cliente existente",
            description = "Endpoint para inativar um cliente pelo ID.")
    @ApiResponses(
            value = { @ApiResponse(responseCode = "200", description = "Cliente inativado com sucesso.") })
    @PutMapping("/inactive/{id}")
    public ResponseEntity<Void> inactiveCustomer(@PathVariable UUID id) throws DocumentNumberException, NotFoundException {
        inactiveCustomerUseCase.execute(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Busca um cliente pelo ID",
            description = "Endpoint para buscar um cliente pelo ID.")
    @ApiResponses(
            value = { @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso.") })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findCustomerById(@PathVariable UUID id) throws NotFoundException {
        Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("operation.type", "findById");
            span.setTag("customer.id", id.toString());
        }
        Customer customer = findCustomerByIdUseCase.execute(id);
        if (span != null && customer != null && customer.getDocumentNumber() != null) {
            span.setTag("customer.document", customer.getDocumentNumber().toString());
        }
        return ResponseEntity.ok().body(customerMapper.toResponse(customer));
    }

    @Operation(
            summary = "Busca um cliente pelo documento.",
            description = "Endpoint para buscar um cliente pelo documento.")
    @ApiResponses(
            value = { @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso.") })
    @GetMapping("/document/{documentNumber}")
    public ResponseEntity<CustomerResponse> findCustomerByDocument(@PathVariable String documentNumber) throws DocumentNumberException, NotFoundException {
        Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("operation.type", "findByDocument");
            span.setTag("customer.document", documentNumber);
        }
        Customer customer = findCustomerByDocumentUseCase.execute(documentNumber);
        if (span != null && customer != null) {
            span.setTag("customer.document", customer.getDocumentNumber().getValue());
        }
        return ResponseEntity.ok().body(customerMapper.toResponse(customer));
    }

    @Operation(
            summary = "Deleta um cliente pelo ID",
            description = "Endpoint para deletar um cliente pelo ID.")
    @ApiResponses(
            value = { @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso.") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) throws DocumentNumberException, NotFoundException {
        Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("operation.type", "delete");
            span.setTag("customer.id", id.toString());
        }
        deleteCustomerUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}