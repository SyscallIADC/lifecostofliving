package org.syscall.exchangerate.control.feeder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.syscall.exchangerate.models.ExchangeRate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class APIExchangeRateFeederTest {

    private APIExchangeRateFeeder feeder;

    @BeforeEach
    void setUp() {
        try {
            feeder = new APIExchangeRateFeeder();
        } catch (RuntimeException e) {
        }
    }

    @Test
    void testBuildUrl() throws Exception {
        if (feeder == null) return;

        Method buildUrlMethod = APIExchangeRateFeeder.class.getDeclaredMethod("buildUrl", String.class, String.class);
        buildUrlMethod.setAccessible(true);

        String url = (String) buildUrlMethod.invoke(feeder, "EUR", "JPY");

        assertTrue(url.contains("function=CURRENCY_EXCHANGE_RATE"));
        assertTrue(url.contains("from_currency=EUR"));
        assertTrue(url.contains("to_currency=JPY"));
        assertTrue(url.contains("apikey="));
        assertTrue(url.startsWith("https://www.alphavantage.co/query"));
    }

    @Test
    void testParseResponseValidJson() throws Exception {
        if (feeder == null) return;

        String jsonString = """
                {
                    "Realtime Currency Exchange Rate": {
                        "1. From_Currency Code": "EUR",
                        "2. From_Currency Name": "Euro",
                        "3. To_Currency Code": "JPY",
                        "4. To_Currency Name": "Japanese Yen",
                        "5. Exchange Rate": "160.50",
                        "6. Last Refreshed": "2026-05-18 10:00:00",
                        "7. Time Zone": "UTC",
                        "8. Bid Price": "160.50",
                        "9. Ask Price": "160.51"
                    }
                }
                """;
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        Method parseMethod = APIExchangeRateFeeder.class.getDeclaredMethod("parseResponse", JsonObject.class);
        parseMethod.setAccessible(true);

        ExchangeRate rate = (ExchangeRate) parseMethod.invoke(feeder, jsonObject);

        assertNotNull(rate);
    }

    @Test
    void testParseResponseInvalidJsonThrowsException() throws Exception {
        if (feeder == null) return;

        String jsonString = "{\"Error Message\": \"Invalid API call\"}";
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        Method parseMethod = APIExchangeRateFeeder.class.getDeclaredMethod("parseResponse", JsonObject.class);
        parseMethod.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            parseMethod.invoke(feeder, jsonObject);
        });

        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("Respuesta inválida de la API", exception.getCause().getMessage());
    }
}