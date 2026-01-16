package com.fiap.core.domain.customer;

import com.fiap.core.domain.Email;
import com.fiap.core.exception.EmailException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Customer {
    private UUID id;
    private String name;
    private DocumentNumber documentNumber;
    private String phone;
    private Email email;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Customer(UUID id, String name, DocumentNumber documentNumber, String phone, String email, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.documentNumber = documentNumber;
        this.phone = phone;
        this.email = new Email(email);
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Customer(UUID id, String name, DocumentNumber documentNumber, String phone, String email) throws EmailException {
        this.id = id;
        this.name = name;
        this.documentNumber = documentNumber;
        this.phone = phone;
        this.email = Email.of(email);
        this.updatedAt = LocalDateTime.now();
    }

    public Customer(String name, DocumentNumber documentNumber, String phone, String email) throws EmailException {
        this.name = name;
        this.documentNumber = documentNumber;
        this.phone = phone;
        this.email = Email.of(email);
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    public Customer(UUID id) {
        this.id =id;
    }

    public Customer() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentNumber getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(DocumentNumber documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void updateFrom(Customer changes) {
        this.name = changes.getName();
        this.email = changes.getEmail();
        this.phone = changes.getPhone();
        this.documentNumber = changes.getDocumentNumber();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;

        return Objects.equals(id, customer.id)
                && Objects.equals(name, customer.name)
                && Objects.equals(documentNumber, customer.documentNumber)
                && Objects.equals(phone, customer.phone)
                && Objects.equals(email, customer.email)
                && Objects.equals(isActive, customer.isActive)
                && Objects.equals(createdAt, customer.createdAt)
                && Objects.equals(updatedAt, customer.updatedAt);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(
                id,
                name,
                documentNumber,
                phone,
                email,
                isActive,
                createdAt,
                updatedAt
        );
    }
}
