package org.syscall.event_store;

import org.syscall.event_store.control.Controller;
import org.syscall.event_store.control.subscriber.ActiveMQSubscriber;
import org.syscall.event_store.control.subscriber.RawEvent;
import org.syscall.event_store.control.subscriber.Subscriber;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) {
        BlockingQueue<RawEvent> queue = new ArrayBlockingQueue<>(20);
        Subscriber subscriber = new ActiveMQSubscriber(queue);
        Controller controller = new Controller(subscriber, queue);
        List<String> topics = List.of("LivingCost", "ExchangeRate");
        controller.run(topics);
    }
}
