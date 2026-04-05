package scheduler;

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