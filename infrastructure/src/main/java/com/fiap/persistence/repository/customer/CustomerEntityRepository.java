package com.fiap.persistence.repository.customer;

import com.fiap.persistence.entity.customer.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerEntityRepository extends JpaRepository<CustomerEntity, UUID> {

    Boolean existsByDocumentNumber(String taxNumber);

    Boolean existsByEmail(String email);

    Optional<CustomerEntity> findByDocumentNumber(String documentNumber);
}
