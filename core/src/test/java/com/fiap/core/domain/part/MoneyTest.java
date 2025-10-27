package com.fiap.core.domain.part;

import com.fiap.core.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void of_withPositiveValue_returnsInstance() throws BusinessRuleException {
        Money m = Money.of(new BigDecimal("10.50"));
        assertNotNull(m);
        assertEquals(new BigDecimal("10.50"), m.getValue());
    }

    @Test
    void of_withZero_returnsInstance() throws BusinessRuleException {
        Money m = Money.of(BigDecimal.ZERO);
        assertNotNull(m);
        assertEquals(BigDecimal.ZERO, m.getValue());
    }

    @Test
    void of_withNull_throwsBusinessRuleException() {
        assertThrows(BusinessRuleException.class, () -> Money.of(null));
    }

    @Test
    void of_withNegative_throwsBusinessRuleException() {
        assertThrows(BusinessRuleException.class, () -> Money.of(new BigDecimal("-0.01")));
    }

    @Test
    void equalsAndHashCode_sameValueSameScale() throws BusinessRuleException {
        Money a = Money.of(new BigDecimal("10.00"));
        Money b = Money.of(new BigDecimal("10.00"));
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotSame(a, b);
    }

    @Test
    void equals_differentScale_notEqual() throws BusinessRuleException {
        Money a = Money.of(new BigDecimal("10.0"));
        Money b = Money.of(new BigDecimal("10.00"));
        assertNotEquals(a, b);
    }
}
