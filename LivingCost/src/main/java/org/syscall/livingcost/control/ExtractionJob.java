package org.syscall.livingcost.control;

import org.syscall.livingcost.control.database.countryQueue.CountryQueueStore;
import org.syscall.livingcost.control.feeder.LivingCostFeeder;
import org.syscall.livingcost.model.LivingCost;

import java.util.Random;

public class ExtractionJob {
    private LivingCostFeeder feeder;
    private Random random;
    private CountryQueueStore countryQueue;

    public ExtractionJob(LivingCostFeeder feeder,
                         CountryQueueStore countryQueue, Random random) {
        this.feeder = feeder;
        this.random = random;
        this.countryQueue = countryQueue;
    }

    public void extraction() {
        addExtraDelay();

        System.out.println("Starting Living Cost Extraction...");
        String nextCountry = countryQueue.getNextCountry();
        if (nextCountry == null) {
            System.out.println("No country found");
            return;
        }
        System.out.println("Extracting data...");
        LivingCost extractedData = feeder.feed(nextCountry);
        System.out.println("Marking data as scrapped...");
        countryQueue.setCountryAsScrapped(nextCountry);
        System.out.println("Iteration finished.");

        System.out.println("Last data extracted from " + nextCountry);
    }

    private void addExtraDelay() {
        try {
            System.out.println("Adding extra delay...");

            int randomExtraDelay = random.nextInt(240000) + 60000;
            Thread.sleep(randomExtraDelay);
        } catch (InterruptedException e) {
            System.out.println("Error in applying extra delay " + e.getMessage());
        }
    }
}
