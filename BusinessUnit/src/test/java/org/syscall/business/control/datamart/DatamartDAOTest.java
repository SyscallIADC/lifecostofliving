package org.syscall.business.control.datamart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.syscall.business.model.CountryStats;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatamartDAOTest {

    private DatamartDAO datamart;

    private static final String LIVING_COST_JSON = """
            {"ts":"2026-05-17T10:00:00Z","ss":"feeder-living-cost","data":{"country":"Spain","currency":"EUR","cappuccino":2.5,"milk":1.0,"rice":1.5,"bread":1.2,"eggs":2.0,"cheese":5.0,"chicken":6.0,"beef":10.0,"fruits":2.0,"vegetables":1.5,"water":0.5,"publicTransport":40.0,"gasoline":1.6,"car":20000.0,"utilities":100.0,"childCare":400.0,"gymMonthly":35.0,"bedroomMonth":800.0,"apartmentMonth":1200.0,"apartmentBuy":3000.0,"salaryMonth":1800.0,"interestRate":3.5}}
            """;

    private static final String EXCHANGE_RATE_JSON = """
            {"ts":"2026-05-17T10:00:00Z","ss":"ExchangeRate-feeder","data":{"fromCurrency":"EUR","toCurrency":"JPY","exchangeRate":160.0,"lastRefreshed":"2026-05-17","timeZone":"UTC"}}
            """;

    @BeforeEach
    void setUp() {
        datamart = new DatamartDAO();
    }

    @Test
    void testUpsertAndGetCountryStats() {
        ParsedEvent event = new ParsedEvent("LivingCost", "feeder-living-cost", "20260517", LIVING_COST_JSON.trim());
        datamart.upsertCountryData(event);

        CountryStats stats = datamart.getCountryStats("Spain");
        assertNotNull(stats, "Debería haber datos de Spain");
        assertEquals("Spain", stats.country());
    }

    @Test
    void testGetCountryStatsNotFound() {
        CountryStats stats = datamart.getCountryStats("CountryThatDoesNotExist");
        assertNull(stats, "Debería devolver null si no hay datos");
    }

    @Test
    void testUpsertExchangeRateAndGet() {
        ParsedEvent event = new ParsedEvent("ExchangeRate", "ExchangeRate-feeder", "20260517", EXCHANGE_RATE_JSON.trim());
        datamart.upsertExchangeRateData(event);

        double rate = datamart.getExchangeRate("EUR", "JPY");
        assertEquals(160.0, rate, 0.01, "El tipo de cambio EUR/JPY debería ser 160.0");
    }

    @Test
    void testGetExchangeRateSameCurrency() {
        double rate = datamart.getExchangeRate("EUR", "EUR");
        assertEquals(1.0, rate, "El tipo de cambio de una moneda consigo misma debe ser 1.0");
    }

    @Test
    void testGetExchangeRateInverse() {
        ParsedEvent event = new ParsedEvent("ExchangeRate", "ExchangeRate-feeder", "20260517", EXCHANGE_RATE_JSON.trim());
        datamart.upsertExchangeRateData(event);

        double rate = datamart.getExchangeRate("JPY", "EUR");
        assertEquals(1.0 / 160.0, rate, 0.001, "El tipo de cambio inverso debería ser 1/160");
    }

    @Test
    void testGetAllCountries() {
        ParsedEvent event = new ParsedEvent("LivingCost", "feeder-living-cost", "20260517", LIVING_COST_JSON.trim());
        datamart.upsertCountryData(event);

        List<CountryStats> all = datamart.getAllCountries();
        assertFalse(all.isEmpty(), "Debería haber al menos un país");
    }

    @Test
    void testUpsertUpdatesExistingCountry() {
        ParsedEvent event1 = new ParsedEvent("LivingCost", "feeder-living-cost", "20260517", LIVING_COST_JSON.trim());
        datamart.upsertCountryData(event1);

        String updatedJson = LIVING_COST_JSON.trim().replace("\"salaryMonth\":1800.0", "\"salaryMonth\":2000.0");
        ParsedEvent event2 = new ParsedEvent("LivingCost", "feeder-living-cost", "20260517", updatedJson);
        datamart.upsertCountryData(event2);

        CountryStats stats = datamart.getCountryStats("Spain");
        assertNotNull(stats);
        assertEquals("Spain", stats.country());
    }
}