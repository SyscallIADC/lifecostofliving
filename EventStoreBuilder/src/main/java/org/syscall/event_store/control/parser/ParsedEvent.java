package org.syscall.event_store.control.parser;

public record ParsedEvent(
        String topic,
        String ss,
        String folderDate,
        String rawJson
) {}
