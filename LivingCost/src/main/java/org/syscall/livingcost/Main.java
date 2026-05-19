package org.syscall.livingcost;

import org.syscall.livingcost.control.LivingCostController;
import org.syscall.livingcost.control.database.countryQueue.CountryQueueInitializer;
import org.syscall.livingcost.control.database.countryQueue.CountryQueueStore;
import org.syscall.livingcost.control.database.countryQueue.SQLiteCountryQueueStore;
import org.syscall.livingcost.control.feeder.LivingCostFeeder;
import org.syscall.livingcost.control.feeder.NumbeoLivingCostFeeder;
import org.syscall.livingcost.control.webScraper.NumbeoHtmlExtractor;
import org.syscall.livingcost.control.webScraper.PlaywrightWebScraper;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String dbPath = args.length > 0 ? args[0] : "livingcost.db";
        CountryQueueInitializer.initialize(dbPath);
        CountryQueueStore countryQueue = new SQLiteCountryQueueStore(args[0]);
        NumbeoHtmlExtractor extractor = new NumbeoHtmlExtractor();
        PlaywrightWebScraper scraper = new PlaywrightWebScraper();
        LivingCostFeeder feeder = new NumbeoLivingCostFeeder(scraper, extractor);
        Random random =  new Random();
        LivingCostController controller = new LivingCostController(feeder, countryQueue, random);
        controller.run();
    }
}
