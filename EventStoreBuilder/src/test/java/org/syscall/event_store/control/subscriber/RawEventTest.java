package org.syscall.event_store.control.subscriber;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RawEventTest {

    @Test
    void testRawEventFields() {
        RawEvent event = new RawEvent("ExchangeRate", "{\"key\":\"value\"}");
        assertEquals("ExchangeRate", event.topic());
        assertEquals("{\"key\":\"value\"}", event.jsonPayload());
    }

    @Test
    void testRawEventEquality() {
        RawEvent event1 = new RawEvent("ExchangeRate", "{}");
        RawEvent event2 = new RawEvent("ExchangeRate", "{}");
        assertEquals(event1, event2);
    }

    @Test
    void testRawEventNotEqual() {
        RawEvent event1 = new RawEvent("ExchangeRate", "{}");
        RawEvent event2 = new RawEvent("LivingCost", "{}");
        assertNotEquals(event1, event2);
    }
}