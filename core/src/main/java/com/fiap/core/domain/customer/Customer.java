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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Customer(UUID id, String name, DocumentNumber documentNumber, String phone, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.documentNumber = documentNumber;
        this.phone = phone;
        this.email = new Email(email);
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

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Customer customer)) return false;

        return getId().equals(customer.getId()) && getName().equals(customer.getName()) && getDocumentNumber().equals(customer.getDocumentNumber()) && getPhone().equals(customer.getPhone()) && getEmail().equals(customer.getEmail()) && getCreatedAt().equals(customer.getCreatedAt()) && Objects.equals(getUpdatedAt(), customer.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getDocumentNumber().hashCode();
        result = 31 * result + getPhone().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getCreatedAt().hashCode();
        result = 31 * result + Objects.hashCode(getUpdatedAt());
        return result;
    }
}
