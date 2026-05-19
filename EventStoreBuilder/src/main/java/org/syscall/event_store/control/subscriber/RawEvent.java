package org.syscall.event_store.control.subscriber;

public record RawEvent(
        String topic,
        String jsonPayload
)
{}
