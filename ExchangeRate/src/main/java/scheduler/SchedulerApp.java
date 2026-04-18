package scheduler;

import consumer.ExchangeRateConsumer;
import database.ExchangeRatePersistance;

public class SchedulerApp {
    public static void main(String[] args) {
        Scheduler scheduler = new ExchangeRateScheduler(
                new ExchangeRateConsumer(),
                new ExchangeRatePersistance()
        );

        scheduler.start();

        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::stop));
    }
}