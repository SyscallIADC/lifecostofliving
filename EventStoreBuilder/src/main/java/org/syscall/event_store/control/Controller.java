package org.syscall.event_store.control;

import org.syscall.event_store.control.file_manager.FileStorageManager;
import org.syscall.event_store.control.parser.EventParser;
import org.syscall.event_store.control.parser.ParsedEvent;
import org.syscall.event_store.control.subscriber.RawEvent;
import org.syscall.event_store.control.subscriber.Subscriber;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Controller {
    private final Subscriber subscriber;
    private final BlockingQueue<RawEvent> queue;
    private boolean isRunning = true;

    public Controller(Subscriber subscriber, BlockingQueue<RawEvent> queue) {
        this.subscriber = subscriber;
        this.queue = queue;
    }

    public void run(List<String> topics) {
        subscriber.start(topics);
        System.out.println("Esperando eventos...");

        while (isRunning) {
            try {
                RawEvent rawEvent = queue.take();

                ParsedEvent parsedEvent = EventParser.parse(rawEvent.jsonPayload(), rawEvent.topic());

                FileStorageManager.save(parsedEvent);

                System.out.println("Evento guardado: " + parsedEvent.topic() + " -> " + parsedEvent.folderDate());
            } catch (InterruptedException e) {
                System.out.println("Error procesando evento " + e.getMessage());
            }
        }
    }

    public void stop() {
        isRunning = false;
        subscriber.stop();
    }
}
