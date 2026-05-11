package syscall.livingcost;

import syscall.livingcost.control.LivingCostController;
import syscall.livingcost.control.database.DatabaseHelper;
import syscall.livingcost.control.database.LivingCostStore;
import syscall.livingcost.control.database.countryQueue.CountryQueueInitializer;
import syscall.livingcost.control.database.countryQueue.CountryQueueStore;
import syscall.livingcost.control.database.countryQueue.SQLiteCountryQueueStore;
import syscall.livingcost.control.feeder.LivingCostFeeder;
import syscall.livingcost.control.feeder.NumbeoLivingCostFeeder;
import syscall.livingcost.control.database.SQLiteLivingCostStore;
import syscall.livingcost.control.webScraper.NumbeoHtmlExtractor;
import syscall.livingcost.control.webScraper.PlaywrightWebScraper;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        DatabaseHelper.initialize(args[0]);
        LivingCostStore store = new SQLiteLivingCostStore(args[0]);
        CountryQueueInitializer.initialize(args[0]);
        CountryQueueStore countryQueue = new SQLiteCountryQueueStore(args[0]);
        NumbeoHtmlExtractor extractor = new NumbeoHtmlExtractor();
        PlaywrightWebScraper scraper = new PlaywrightWebScraper();
        LivingCostFeeder feeder = new NumbeoLivingCostFeeder(scraper, extractor);
        Random random =  new Random();
        LivingCostController controller = new LivingCostController(store, feeder, countryQueue, random);
        controller.run();
    }
}
