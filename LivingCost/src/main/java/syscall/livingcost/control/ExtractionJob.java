package syscall.livingcost.control;

import syscall.livingcost.control.database.LivingCostStore;
import syscall.livingcost.control.database.countryQueue.CountryQueueStore;
import syscall.livingcost.control.feeder.LivingCostFeeder;
import syscall.livingcost.model.LivingCost;

import java.util.Random;

public class ExtractionJob {
    private LivingCostStore store;
    private LivingCostFeeder feeder;
    private Random random;
    private CountryQueueStore countryQueue;

    public ExtractionJob(LivingCostStore store, LivingCostFeeder feeder,
                         CountryQueueStore countryQueue, Random random) {
        this.store = store;
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
        System.out.println("Saving data...");
        store.insertData(extractedData);
        System.out.println("Marking data as scrapped...");
        countryQueue.setCountryAsScrapped(nextCountry);
        System.out.println("Iteration finished.");

        System.out.println("Last data extracted from " + nextCountry);
        LivingCost last = store.retrieveLastData();
        System.out.println("Last data currency " + last.currency() + " and cappuccino price: " + last.cappuccino());
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
