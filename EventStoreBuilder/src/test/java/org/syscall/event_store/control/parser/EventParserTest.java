package org.syscall.event_store.control.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventParserTest {

    private static final String VALID_JSON = "{\"ts\":\"2026-05-17T10:00:00Z\",\"ss\":\"ExchangeRate-feeder\",\"data\":{\"fromCurrency\":\"EUR\"}}";

    @Test
    void testParseValidJson() {
        ParsedEvent event = EventParser.parse(VALID_JSON, "ExchangeRate");
        assertNotNull(event);
        assertEquals("ExchangeRate", event.topic());
        assertEquals("ExchangeRate-feeder", event.ss());
        assertEquals("20260517", event.folderDate());
        assertEquals(VALID_JSON, event.rawJson());
    }

    @Test
    void testParseExtractsSsCorrectly() {
        ParsedEvent event = EventParser.parse(VALID_JSON, "ExchangeRate");
        assertEquals("ExchangeRate-feeder", event.ss());
    }

    @Test
    void testParseExtractsFolderDateCorrectly() {
        ParsedEvent event = EventParser.parse(VALID_JSON, "ExchangeRate");
        assertEquals("20260517", event.folderDate());
    }

    @Test
    void testParseDifferentTopic() {
        String json = "{\"ts\":\"2026-05-17T10:00:00Z\",\"ss\":\"feeder-living-cost\",\"data\":{}}";
        ParsedEvent event = EventParser.parse(json, "LivingCost");
        assertEquals("LivingCost", event.topic());
        assertEquals("feeder-living-cost", event.ss());
    }

    @Test
    void testParseInvalidJsonThrowsException() {
        assertThrows(Exception.class, () -> EventParser.parse("invalid json", "ExchangeRate"));
    }
}