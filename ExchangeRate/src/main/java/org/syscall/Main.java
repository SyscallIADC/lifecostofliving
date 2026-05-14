package org.syscall;

import org.syscall.control.Controller;
import org.syscall.control.feeder.APIExchangeRateFeeder;
import org.syscall.control.publisher.ActiveMQExchangeRatePublisher;

public class Main {
    public static void main(String[] args) {
        ActiveMQExchangeRatePublisher publisher = new ActiveMQExchangeRatePublisher();
        APIExchangeRateFeeder feeder = new APIExchangeRateFeeder(publisher);
        Controller controller = new Controller(feeder, publisher);

        Runtime.getRuntime().addShutdownHook(new Thread(controller::stop));

        controller.start();
    }
}