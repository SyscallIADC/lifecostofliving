package scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import database.DatabaseHelper;
import models.LivingCost;
import consumer.WebScrapper;

import java.util.Random;

@Component
public class LivingCostExtraction{
    private final Random random = new Random();

    public LivingCostExtraction() {
    }

    @Scheduled(fixedDelay = 900000)
    public void run() {
        try {
            System.out.println("Adding extra delay...");
            int randomExtraDelay = random.nextInt(240000) + 60000;
            Thread.sleep(randomExtraDelay);
        } catch (InterruptedException e) {
            System.out.println("Error in applying extra delay " + e.getMessage());
        }

        System.out.println("Starting Living Cost Extraction...");
        String nextContry = DatabaseHelper.getInstance().getNextCountry();

        if (nextContry == null) {
            System.out.println("No country found");
            return;
        }

        try (WebScrapper scrapper = new WebScrapper()) {
            System.out.println("Scrapping Living Cost Extraction...");
            LivingCost data = scrapper.extractDataForCountry(nextContry);

            System.out.println("Saving data in the database...");
            DatabaseHelper.getInstance().insertCostOfLiving(data);
            DatabaseHelper.getInstance().setCountryAsScrapped(nextContry);

            System.out.println("Correctly saved Living Cost Extraction");
        } catch (Exception e) {
            System.out.println("Error in saving Living Cost Extraction");
            e.printStackTrace();
        }

        System.out.println("Finished Living Cost Extraction");
    }
}
