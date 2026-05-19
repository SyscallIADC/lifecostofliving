package org.syscall.control.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class APIConsumerTest {

    @Test
    void testFetchThrowsOnBadUrl() {
        APIConsumer consumer = new APIConsumer();
        assertThrows(Exception.class, () -> consumer.fetch("http://localhost:0/noexiste"));
    }

    @Test
    void testFetchThrowsOnInvalidUrl() {
        APIConsumer consumer = new APIConsumer();
        assertThrows(Exception.class, () -> consumer.fetch("not-a-url"));
    }
}