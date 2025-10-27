package com.fiap.core.domain.user;

import com.fiap.core.exception.EmailException;
import com.fiap.core.exception.PasswordException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static String anyValidRole() {
        return UserRole.values()[0].name();
    }

    @Test
    void shouldCreateUserWithValidData_fullCtor() throws EmailException, PasswordException {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime updated = created.plusSeconds(1);

        User u = new User(
                id,
                "Alice",
                "alice@example.com",
                anyValidRole(),
                "Abcdef1!",
                created,
                updated
        );

        assertEquals(id, u.getId());
        assertEquals("Alice", u.getName());
        assertEquals("alice@example.com", u.getEmail());
        assertNotNull(u.getPassword());
        assertEquals(anyValidRole(), u.getRole().name());
        assertEquals(created, u.getCreatedAt());
        assertEquals(updated, u.getUpdatedAt());
    }

    @Test
    void shouldCreateUserWithNullPassword_thenSetLater() throws EmailException, PasswordException {
        UUID id = UUID.randomUUID();
        User u = new User(
                id,
                "Bob",
                "bob@example.com",
                anyValidRole(),
                null
        );
        u.setPassword("Zxcvb1!A");
        assertEquals("Zxcvb1!A", u.getPassword());
        assertEquals("bob@example.com", u.getEmail());
        assertEquals(anyValidRole(), u.getRole().name());
    }

    @Test
    void shouldFailWhenEmailIsInvalid_inCtorThatValidates() {
        assertThrows(EmailException.class, () ->
                new User(UUID.randomUUID(), "Eve", "bad-email", anyValidRole(), "Abcdef1!")
        );
    }

    @Test
    void shouldFailWhenPasswordInvalid_inCtor() {
        assertThrows(PasswordException.class, () ->
                new User(UUID.randomUUID(), "Eve", "eve@example.com", anyValidRole(), "weak")
        );
    }

    @Test
    void shouldFailWhenRoleIsInvalid_inCtor() {
        assertThrows(IllegalArgumentException.class, () ->
                new User(UUID.randomUUID(), "Eve", "eve@example.com", "NOT_A_ROLE", "Abcdef1!")
        );
    }

    @Test
    void setEmailDoesNotValidate_formatIsAccepted() throws EmailException, PasswordException {
        User u = new User(UUID.randomUUID(), "Dana", "dana@example.com", anyValidRole(), "Abcdef1!");
        assertDoesNotThrow(() -> u.setEmail("not-an-email"));
        assertEquals("not-an-email", u.getEmail());
    }

    @Test
    void equalsIsReflexiveAndHashCodeStable() throws EmailException, PasswordException {
        User u = new User(UUID.randomUUID(), "Mary", "mary@example.com", anyValidRole(), "Abcdef1!");
        assertEquals(u, u);
        int h1 = u.hashCode();
        int h2 = u.hashCode();
        assertEquals(h1, h2);
    }

    @Test
    void differentUsersAreNotEqual_evenIfFieldsSimilar() throws EmailException, PasswordException {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updated = LocalDateTime.of(2024, 1, 1, 10, 5);

        User a = new User(id, "Mary", "mary@example.com", anyValidRole(), "Abcdef1!", created, updated);
        User b = new User(id, "Mary", "mary@example.com", anyValidRole(), "Abcdef1!", created, updated);

        assertNotEquals(a, b);
    }
}
