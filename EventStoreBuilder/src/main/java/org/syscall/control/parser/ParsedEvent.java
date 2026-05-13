package org.syscall.control.parser;

public record ParsedEvent(
        String topic,
        String ss,
        String folderDate,
        String rawJson
) {}
