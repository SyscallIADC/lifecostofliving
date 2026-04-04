package scheduler;
import database.DatabaseHelper;
import models.LivingCost;
import consumer.WebScrapper;

public class LivingCostExtraction implements Runnable {
    public LivingCostExtraction() {
    }

    @Override
    public void run() {
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
