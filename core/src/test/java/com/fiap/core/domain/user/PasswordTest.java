package com.fiap.core.domain.user;

import com.fiap.core.exception.PasswordException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    void shouldCreatePasswordWhenValid() {
        assertDoesNotThrow(() -> new Password("Abcdef1!"));
        Password p = assertDoesNotThrow(() -> new Password("Zyxwvut9@"));
        assertEquals("Zyxwvut9@", p.getValue());
    }

    @Test
    void shouldFailWhenNull() {
        PasswordException ex = assertThrows(PasswordException.class, () -> new Password(null));
        assertEquals(ErrorCodeEnum.USE0001.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.USE0001.getMessage(), ex.getMessage());
    }

    @Test
    void shouldFailWhenTooShort() {
        PasswordException ex = assertThrows(PasswordException.class, () -> new Password("A1!a"));
        assertEquals(ErrorCodeEnum.USE0002.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.USE0002.getMessage(), ex.getMessage());
    }

    @Test
    void shouldFailWhenMissingUppercase() {
        PasswordException ex = assertThrows(PasswordException.class, () -> new Password("abcdef1!"));
        assertEquals(ErrorCodeEnum.USE0003.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.USE0003.getMessage(), ex.getMessage());
    }

    @Test
    void shouldFailWhenMissingLowercase() {
        PasswordException ex = assertThrows(PasswordException.class, () -> new Password("ABCDEF1!"));
        assertEquals(ErrorCodeEnum.USE0004.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.USE0004.getMessage(), ex.getMessage());
    }

    @Test
    void shouldFailWhenMissingDigit() {
        PasswordException ex = assertThrows(PasswordException.class, () -> new Password("Abcdefg!"));
        assertEquals(ErrorCodeEnum.USE0005.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.USE0005.getMessage(), ex.getMessage());
    }

    @Test
    void shouldFailWhenMissingSpecial() {
        PasswordException ex = assertThrows(PasswordException.class, () -> new Password("Abcdefg1"));
        assertEquals(ErrorCodeEnum.USE0006.getCode(), ex.getCode());
        assertEquals(ErrorCodeEnum.USE0006.getMessage(), ex.getMessage());
    }
}
