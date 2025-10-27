package com.fiap.core.domain.customer;

import com.fiap.core.exception.EmailException;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private DocumentNumber doc() {
        return DocumentNumber.fromPersistence("12345678901");
    }

    @Test
    void shouldCreateCustomerWithAllArgsConstructor() throws EmailException {
        UUID id = UUID.randomUUID();
        String name = "Alice";
        DocumentNumber document = doc();
        String phone = "11999999999";
        String email = "alice@example.com";
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 10, 12, 30);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 11, 8, 0);

        Customer c = new Customer(id, name, document, phone, email, createdAt, updatedAt);

        assertEquals(id, c.getId());
        assertEquals(name, c.getName());
        assertEquals(document, c.getDocumentNumber());
        assertEquals(phone, c.getPhone());
        assertEquals(email, c.getEmail().getValue());
        assertEquals(createdAt, c.getCreatedAt());
        assertEquals(updatedAt, c.getUpdatedAt());
    }

    @Test
    void shouldCreateCustomerSettingUpdatedAtOnly() throws EmailException {
        UUID id = UUID.randomUUID();
        Customer c = new Customer(id, "Bob", doc(), "11888888888", "bob@example.com");

        assertEquals(id, c.getId());
        assertNull(c.getCreatedAt());
        assertNotNull(c.getUpdatedAt());
        assertEquals("bob@example.com", c.getEmail().getValue());
    }

    @Test
    void shouldCreateCustomerSettingCreatedAtOnly() throws EmailException {
        Customer c = new Customer("Carol", doc(), "11777777777", "carol@example.com");

        assertNotNull(c.getCreatedAt());
        assertNull(c.getUpdatedAt());
        assertEquals("carol@example.com", c.getEmail().getValue());
    }

    @Test
    void shouldThrowWhenEmailIsInvalidOnCtorWithUpdatedAt() {
        UUID id = UUID.randomUUID();
        assertThrows(EmailException.class, () ->
                new Customer(id, "Dave", doc(), "11666666666", "not-an-email"));
    }

    @Test
    void shouldThrowWhenEmailIsInvalidOnCtorWithCreatedAt() {
        assertThrows(EmailException.class, () ->
                new Customer("Erin", doc(), "11555555555", "bad_email"));
    }

    @Test
    void equalsAndHashCodeShouldMatchForIdenticalCustomers() throws EmailException {
        UUID id = UUID.randomUUID();
        DocumentNumber d = doc();
        LocalDateTime created = LocalDateTime.of(2024, 2, 1, 9, 0);
        LocalDateTime updated = LocalDateTime.of(2024, 2, 2, 10, 0);

        Customer a = new Customer(id, "Frank", d, "11444444444", "frank@example.com", created, updated);
        Customer b = new Customer(id, "Frank", d, "11444444444", "frank@example.com", created, updated);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equalsShouldFailWhenAnyRelevantFieldDiffers() throws EmailException {
        UUID id = UUID.randomUUID();
        DocumentNumber d = doc();
        LocalDateTime created = LocalDateTime.of(2024, 3, 1, 9, 0);
        LocalDateTime updated = LocalDateTime.of(2024, 3, 2, 10, 0);

        Customer base = new Customer(id, "Gina", d, "11333333333", "gina@example.com", created, updated);
        Customer differentPhone = new Customer(id, "Gina", d, "11000000000", "gina@example.com", created, updated);

        assertNotEquals(base, differentPhone);
    }

    @Test
    void settersShouldMutateFields() throws EmailException {
        Customer c = new Customer(UUID.randomUUID(), "Hank", doc(), "11222222222", "hank@example.com");
        c.setName("Hank Jr.");
        c.setPhone("11111111111");
        c.setUpdatedAt(LocalDateTime.of(2024, 4, 1, 10, 0));

        assertEquals("Hank Jr.", c.getName());
        assertEquals("11111111111", c.getPhone());
        assertEquals(LocalDateTime.of(2024, 4, 1, 10, 0), c.getUpdatedAt());
    }
}
