package syscall.livingcost.control.webScraper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaywrightWebScraperTest {
    private PlaywrightWebScraper scraper;

    @BeforeEach
    void setUp() throws InterruptedException {
        System.out.println("Esperando 10 segundos...");
        Thread.sleep(10000);
        scraper = new PlaywrightWebScraper();
    }

    @AfterEach
    void tearDown() {
        if (scraper != null) {
            scraper.close();
        }
    }

    @Test
    void testFetchRealHtmlFromNumbeo() {
        String targetUrl = "https://www.numbeo.com/cost-of-living/country_result.jsp?country=Spain";
        System.out.println("Navigation to Numbeo...");
        String htmlContent = scraper.getHtmlContent(targetUrl);

        assertNotNull(htmlContent);
        assertTrue(htmlContent.toLowerCase().contains("<html"));
        assertTrue(htmlContent.toLowerCase().contains("<body"));
        assertTrue(htmlContent.contains("EUR"));
        assertTrue(htmlContent.contains("Spain"));
    }

    @Test
    void testFetchHtmlFromWikipedia() {
        String targetUrl = "https://en.wikipedia.org/wiki/Web_scraping";
        System.out.println("Navigation to Wikipedia...");
        String htmlContent = scraper.getHtmlContent(targetUrl);

        assertNotNull(htmlContent);
        assertTrue(htmlContent.contains("Web scraping"));
    }
}
