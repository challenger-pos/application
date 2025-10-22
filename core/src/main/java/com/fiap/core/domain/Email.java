package com.fiap.core.domain;

import com.fiap.core.exception.EmailException;

import java.util.Objects;

public class Email {
    private String value;

    public Email(String value) {
        this.value = value;
    }

    public static Email of(String value) throws EmailException {
        if (!isValidEmail(value)) {
            throw new EmailException("Formato de email inválido", "CAD0011");
        }
        return new Email(value);
    }

    public String getValue() {
        return value;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Email email)) return false;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
