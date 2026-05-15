package org.syscall.control.publisher;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.syscall.models.ExchangeRate;

import static org.junit.jupiter.api.Assertions.*;

class SerializerTest {

    private final Serializer serializer = new Serializer();

    @Test
    void testSerializeContainsAllFields() {
        ExchangeRate rate = new ExchangeRate("EUR", "USD", 1.16, "2026-05-15", "UTC");
        String json = serializer.serialize(rate, "test-source");

        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        assertEquals("test-source", obj.get("ss").getAsString());
        assertEquals("EUR", obj.get("from_currency").getAsString());
        assertEquals("USD", obj.get("to_currency").getAsString());
        assertEquals(1.16, obj.get("exchange_rate").getAsDouble());
        assertEquals("UTC", obj.get("time_zone").getAsString());
        assertNotNull(obj.get("ts"));
    }
}