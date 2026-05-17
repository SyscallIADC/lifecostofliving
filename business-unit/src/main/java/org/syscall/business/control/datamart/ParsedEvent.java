package org.syscall.business.control.datamart;

public record ParsedEvent(
        String topic,
        String ss,
        String folderDate,
        String rawJson
) {}
