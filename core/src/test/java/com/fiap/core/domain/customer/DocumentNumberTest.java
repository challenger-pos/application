package com.fiap.core.domain.customer;

import com.fiap.core.exception.DocumentNumberException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentNumberTest {

    @Test
    void of_shouldCleanFormattingAndKeepOnlyDigits_forValidCpf() throws Exception {
        DocumentNumber dn = DocumentNumber.of("529.982.247-25");
        assertEquals("52998224725", dn.getValue());
        assertEquals("52998224725", dn.toString());
    }

    @Test
    void of_shouldCreateForValidCnpj() throws Exception {
        DocumentNumber dn = DocumentNumber.of("04.252.011/0001-10");
        assertEquals("04252011000110", dn.getValue());
    }

    @Test
    void of_shouldThrowForNullOrBlank() {
        DocumentNumberException ex1 = assertThrows(DocumentNumberException.class, () -> DocumentNumber.of(null));
        assertEquals(ErrorCodeEnum.CAD0001.getCode(), ex1.getCode());

        DocumentNumberException ex2 = assertThrows(DocumentNumberException.class, () -> DocumentNumber.of("   "));
        assertEquals(ErrorCodeEnum.CAD0001.getCode(), ex2.getCode());
    }

    @Test
    void of_shouldThrowForCpfWithWrongChecksum() {
        DocumentNumberException ex = assertThrows(DocumentNumberException.class, () -> DocumentNumber.of("529.982.247-24"));
        assertEquals(ErrorCodeEnum.CAD0001.getCode(), ex.getCode());
    }

    @Test
    void of_shouldThrowForCpfWithAllSameDigits() {
        DocumentNumberException ex = assertThrows(DocumentNumberException.class, () -> DocumentNumber.of("111.111.111-11"));
        assertEquals(ErrorCodeEnum.CAD0001.getCode(), ex.getCode());
    }

    @Test
    void of_shouldThrowForCnpjWithWrongChecksum() {
        DocumentNumberException ex = assertThrows(DocumentNumberException.class, () -> DocumentNumber.of("00.000.000/0000-00"));
        assertEquals(ErrorCodeEnum.CAD0001.getCode(), ex.getCode());
    }

    @Test
    void fromPersistence_shouldBypassValidation() {
        DocumentNumber dn = DocumentNumber.fromPersistence("not-a-valid-doc");
        assertEquals("not-a-valid-doc", dn.getValue());
    }

    @Test
    void equalsAndHashCode_shouldConsiderCleanedValue() throws Exception {
        DocumentNumber a = DocumentNumber.of("529.982.247-25");
        DocumentNumber b = DocumentNumber.of("52998224725");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
