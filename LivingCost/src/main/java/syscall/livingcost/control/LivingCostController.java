package syscall.livingcost.control;

import syscall.livingcost.control.database.LivingCostStore;
import syscall.livingcost.control.database.countryQueue.CountryQueueStore;
import syscall.livingcost.control.feeder.LivingCostFeeder;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LivingCostController {
    private final LivingCostFeeder feeder;
    private final LivingCostStore store;
    private final CountryQueueStore countryQueue;
    private final Random random;

    public LivingCostController(LivingCostStore store, LivingCostFeeder feeder,
                                CountryQueueStore countryQueue, Random random) {
        this.feeder = feeder;
        this.store = store;
        this.countryQueue = countryQueue;
        this.random = random;
    }

    public void run() {
        ExtractionJob extractionJob = new ExtractionJob(store, feeder, countryQueue, random);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        System.out.println("Starting LivingCost Module");

        scheduler.scheduleAtFixedRate(
                () -> {try {
                    extractionJob.extraction();
                } catch (Exception e) {
                    System.out.println("Error during extraction... " + e.getMessage());
                }
                },
                0,
                15,
                TimeUnit.MINUTES
        );
    }
}
