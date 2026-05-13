package syscall.livingcost.control.feeder;

import syscall.livingcost.control.event.LivingCostEvent;
import syscall.livingcost.control.publisher.PublisherHelper;
import syscall.livingcost.control.webScraper.NumbeoHtmlExtractor;
import syscall.livingcost.control.webScraper.NumbeoLivingCostData;
import syscall.livingcost.control.webScraper.WebScraper;
import syscall.livingcost.model.LivingCost;
import javax.jms.JMSException;

public class NumbeoLivingCostFeeder implements LivingCostFeeder {
    private static final String BASE_URL = "https://www.numbeo.com/cost-of-living/country_result.jsp?country=";
    private final WebScraper scraper;
    private final NumbeoHtmlExtractor extractor;
    public NumbeoLivingCostFeeder(WebScraper scraper, NumbeoHtmlExtractor extractor) {
        this.scraper = scraper;
        this.extractor = extractor;
    }
    @Override
    public LivingCost feed(String countryName) {
        String countryUrl = countryName.replace(" ", "+");
        String targetUrl = BASE_URL + countryUrl;

        String page = scraper.getHtmlContent(targetUrl);
        NumbeoLivingCostData extractedData = extractor.extract(page);
        LivingCost data = LivingCostFactory.fromNumbeoData(extractedData, countryName);
        LivingCostEvent event = LivingCostEvent.from("feeder-living-cost", data);
        try {
            PublisherHelper.publishEvent("LivingCost", event);
            System.out.println("Evento enviado con éxito.");
        } catch (JMSException e) {
            System.err.println("Error publicando: " + e.getMessage());
        }
        return data;
    }
}
