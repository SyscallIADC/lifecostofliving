package org.syscall.models;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateParserTest {

    private final ExchangeRateParser parser = new ExchangeRateParser();

    @Test
    void testToMap() {
        ExchangeRate rate = new ExchangeRate("EUR", "USD", 1.16, "2026-05-15", "UTC");
        Map<String, Object> map = parser.toMap(rate);

        assertEquals("EUR", map.get("from-currency"));
        assertEquals("USD", map.get("to-currency"));
        assertEquals(1.16, map.get("exchange-rate"));
        assertEquals("2026-05-15", map.get("last-refreshed"));
        assertEquals("UTC", map.get("time-zone"));
    }

    @Test
    void testFromMap() {
        Map<String, Object> map = Map.of(
                "from-currency", "EUR",
                "to-currency", "USD",
                "exchange-rate", 1.16,
                "last-refreshed", "2026-05-15",
                "time-zone", "UTC"
        );

        ExchangeRate rate = parser.fromMap(map);

        assertEquals("EUR", rate.getFromCurrency());
        assertEquals("USD", rate.getToCurrency());
        assertEquals(1.16, rate.getExchangeRate());
    }

    @Test
    void testExtractDoubleFromInteger() {
        assertEquals(1.0, ExchangeRateParser.extractDouble(1));
    }

    @Test
    void testExtractDoubleFromDouble() {
        assertEquals(1.16, ExchangeRateParser.extractDouble(1.16));
    }

    @Test
    void testExtractDoubleFromInvalidType() {
        assertEquals(0.0, ExchangeRateParser.extractDouble("invalid"));
    }
}