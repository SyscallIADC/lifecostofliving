package scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import database.DatabaseHelper;

@SpringBootApplication
@EnableScheduling
public class Scheduler {
    public static void main(String[] args) {
        DatabaseHelper.getInstance();

        SpringApplication.run(Scheduler.class, args);
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private final ExchangeRateExtraction extraction;

    public Scheduler(ExchangeRateExtraction extraction) {
        this.extraction = extraction;
    }

    @Scheduled(cron = "0 0 8 * * ?", zone = "Europe/Madrid")
    public void runDailyTask() {
        extraction.execute();
    }
}