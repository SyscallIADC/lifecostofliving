package org.syscall.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateTest {

    @Test
    void testConstructorAndGetters() {
        ExchangeRate rate = new ExchangeRate("EUR", "USD", 1.16, "2026-05-15 10:00:00", "UTC");

        assertEquals("EUR", rate.getFromCurrency());
        assertEquals("USD", rate.getToCurrency());
        assertEquals(1.16, rate.getExchangeRate());
        assertEquals("2026-05-15 10:00:00", rate.getLastRefreshed());
        assertEquals("UTC", rate.getTimeZone());
    }
}