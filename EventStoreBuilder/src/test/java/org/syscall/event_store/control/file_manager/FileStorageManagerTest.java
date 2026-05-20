package org.syscall.event_store.control.file_manager;

import org.junit.jupiter.api.Test;
import org.syscall.event_store.control.parser.ParsedEvent;

import static org.junit.jupiter.api.Assertions.*;

public class FileStorageManagerTest {

    @Test
    void testSaveCreatesFile() {
        ParsedEvent event = new ParsedEvent("ExchangeRate", "ExchangeRate-feeder", "20260517", "{\"ts\":\"2026-05-17T10:00:00Z\"}");
        assertDoesNotThrow(() -> FileStorageManager.save(event));
    }

    @Test
    void testSaveAppendsMultipleEvents() {
        ParsedEvent event1 = new ParsedEvent("ExchangeRate", "ExchangeRate-feeder", "20260518", "{\"ts\":\"2026-05-18T10:00:00Z\",\"event\":\"1\"}");
        ParsedEvent event2 = new ParsedEvent("ExchangeRate", "ExchangeRate-feeder", "20260518", "{\"ts\":\"2026-05-18T10:00:00Z\",\"event\":\"2\"}");

        assertDoesNotThrow(() -> {
            FileStorageManager.save(event1);
            FileStorageManager.save(event2);
        });
    }

    @Test
    void testSaveDifferentTopics() {
        ParsedEvent exchangeEvent = new ParsedEvent("ExchangeRate", "ExchangeRate-feeder", "20260519", "{\"ts\":\"2026-05-19T10:00:00Z\"}");
        ParsedEvent livingEvent = new ParsedEvent("LivingCost", "feeder-living-cost", "20260519", "{\"ts\":\"2026-05-19T10:00:00Z\"}");

        assertDoesNotThrow(() -> {
            FileStorageManager.save(exchangeEvent);
            FileStorageManager.save(livingEvent);
        });
    }
}