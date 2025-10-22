package com.fiap.core.domain.part;

import com.fiap.core.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void of_withValidValues_createsInstance() throws BusinessRuleException {
        Stock s = Stock.of(10, 2, 1);
        assertEquals(10, s.getStockQuantity());
        assertEquals(2, s.getReservedStock());
        assertEquals(1, s.getMinimumStock());
    }

    @Test
    void of_withNegativeValues_throws() {
        assertThrows(BusinessRuleException.class, () -> Stock.of(-1, 0, 0));
        assertThrows(BusinessRuleException.class, () -> Stock.of(0, -1, 0));
        assertThrows(BusinessRuleException.class, () -> Stock.of(0, 0, -1));
    }

    @Test
    void subtract_happyPath_movesFromStockToReserved() throws BusinessRuleException {
        Stock s = Stock.of(10, 0, 0);
        s.subtract(3);
        assertEquals(7, s.getStockQuantity());
        assertEquals(3, s.getReservedStock());
    }

    @Test
    void subtract_zeroOrNegative_throws() throws BusinessRuleException {
        Stock s = Stock.of(10, 0, 0);
        assertThrows(BusinessRuleException.class, () -> s.subtract(0));
        assertThrows(BusinessRuleException.class, () -> s.subtract(-5));
    }

    @Test
    void subtract_moreThanAvailable_throwsAndDoesNotChange() throws BusinessRuleException {
        Stock s = Stock.of(2, 1, 0);
        assertThrows(BusinessRuleException.class, () -> s.subtract(3));
        assertEquals(2, s.getStockQuantity());
        assertEquals(1, s.getReservedStock());
    }

    @Test
    void restore_happyPath_movesFromReservedBackToStock() throws BusinessRuleException {
        Stock s = Stock.of(5, 4, 0);
        s.restore(3);
        assertEquals(8, s.getStockQuantity());
        assertEquals(1, s.getReservedStock());
    }

    @Test
    void restore_zeroOrNegative_throws() throws BusinessRuleException {
        Stock s = Stock.of(5, 2, 0);
        assertThrows(BusinessRuleException.class, () -> s.restore(0));
        assertThrows(BusinessRuleException.class, () -> s.restore(-2));
    }

    @Test
    void restore_moreThanReserved_throwsAndDoesNotChange() throws BusinessRuleException {
        Stock s = Stock.of(5, 2, 0);
        assertThrows(BusinessRuleException.class, () -> s.restore(3));
        assertEquals(5, s.getStockQuantity());
        assertEquals(2, s.getReservedStock());
    }

    @Test
    void subtractReservedStock_decreasesReservedOnly() throws BusinessRuleException {
        Stock s = Stock.of(5, 4, 0);
        s.subtractReservedStock(2);
        assertEquals(5, s.getStockQuantity());
        assertEquals(2, s.getReservedStock());
    }
}
