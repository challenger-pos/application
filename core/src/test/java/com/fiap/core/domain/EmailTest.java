package com.fiap.core.domain;

import com.fiap.core.exception.EmailException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void of_withValidEmail_createsInstance() throws EmailException {
        Email e = Email.of("user.name+tag99@test-domain.com");
        assertEquals("user.name+tag99@test-domain.com", e.getValue());
    }

    @Test
    void of_withUppercase_isAccepted() throws EmailException {
        Email e = Email.of("USER@EXAMPLE.COM");
        assertEquals("USER@EXAMPLE.COM", e.getValue());
    }

    @Test
    void of_withNull_throws() {
        assertThrows(EmailException.class, () -> Email.of(null));
    }

    @Test
    void of_withoutAt_throws() {
        assertThrows(EmailException.class, () -> Email.of("user.example.com"));
    }

    @Test
    void of_withoutDomainTld_throws() {
        assertThrows(EmailException.class, () -> Email.of("user@example"));
    }

    @Test
    void of_withSpace_throws() {
        assertThrows(EmailException.class, () -> Email.of("user name@example.com"));
    }

    @Test
    void equalsAndHashCode_basedOnValue() throws EmailException {
        Email a = Email.of("a@b.com");
        Email b = Email.of("a@b.com");
        Email c = Email.of("x@y.com");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void constructor_allowsBypassingValidation() throws EmailException {
        Email e = new Email("not-an-email");
        assertEquals("not-an-email", e.getValue());
        assertNotEquals(e, Email.of("user@example.com"));
    }
}
