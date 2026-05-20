package org.syscall.event_store.control;

import org.junit.jupiter.api.Test;
import org.syscall.event_store.control.subscriber.RawEvent;
import org.syscall.event_store.control.subscriber.Subscriber;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @Test
    void testStopController() {
        BlockingQueue<RawEvent> queue = new ArrayBlockingQueue<>(10);
        Subscriber mockSubscriber = new Subscriber() {
            @Override
            public void start(List<String> topics) {}
            @Override
            public void stop() {}
        };

        Controller controller = new Controller(mockSubscriber, queue);
        assertDoesNotThrow(controller::stop);
    }

    @Test
    void testControllerProcessesEvent() throws InterruptedException {
        BlockingQueue<RawEvent> queue = new ArrayBlockingQueue<>(10);
        Subscriber mockSubscriber = new Subscriber() {
            @Override
            public void start(List<String> topics) {}
            @Override
            public void stop() {}
        };

        Controller controller = new Controller(mockSubscriber, queue);

        String validJson = "{\"ts\":\"2026-05-17T10:00:00Z\",\"ss\":\"ExchangeRate-feeder\",\"data\":{}}";
        queue.put(new RawEvent("ExchangeRate", validJson));

        Thread thread = new Thread(() -> controller.run(List.of("ExchangeRate")));
        thread.start();
        Thread.sleep(500);
        controller.stop();
        thread.interrupt();

        assertTrue(queue.isEmpty(), "La cola debería estar vacía después de procesar el evento");
    }
}