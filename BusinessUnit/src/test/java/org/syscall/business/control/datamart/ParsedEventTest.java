package org.syscall.business.control.datamart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParsedEventTest {

    @Test
    void testParsedEventFields() {
        ParsedEvent event = new ParsedEvent("ExchangeRate", "ExchangeRate-feeder", "20260517", "{\"key\":\"value\"}");

        assertEquals("ExchangeRate", event.topic());
        assertEquals("ExchangeRate-feeder", event.ss());
        assertEquals("20260517", event.folderDate());
        assertEquals("{\"key\":\"value\"}", event.rawJson());
    }

    @Test
    void testParsedEventEquality() {
        ParsedEvent event1 = new ParsedEvent("ExchangeRate", "ExchangeRate-feeder", "20260517", "{}");
        ParsedEvent event2 = new ParsedEvent("ExchangeRate", "ExchangeRate-feeder", "20260517", "{}");

        assertEquals(event1, event2);
    }
}