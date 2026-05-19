package org.syscall.exchangerate;

import org.syscall.exchangerate.control.publisher.ActiveMQExchangeRatePublisher;
import org.syscall.exchangerate.control.Controller;
import org.syscall.exchangerate.control.feeder.APIExchangeRateFeeder;
import org.syscall.exchangerate.control.feeder.ExchangeRateFeeder;

public class Main {
    public static void main(String[] args) {
        ExchangeRateFeeder feeder = new APIExchangeRateFeeder();
        ActiveMQExchangeRatePublisher publisher = new ActiveMQExchangeRatePublisher();
        Controller controller = new Controller(feeder, publisher);

        Runtime.getRuntime().addShutdownHook(new Thread(controller::stop));

        controller.start();
    }
}