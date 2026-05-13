package org.syscall.control.subscriber;

public record RawEvent(
        String topic,
        String jsonPayload
)
{}
