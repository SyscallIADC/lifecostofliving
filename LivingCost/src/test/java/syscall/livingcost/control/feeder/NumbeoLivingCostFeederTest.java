package syscall.livingcost.control.feeder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.syscall.livingcost.control.feeder.NumbeoLivingCostFeeder;
import org.syscall.livingcost.control.webScraper.NumbeoHtmlExtractor;
import org.syscall.livingcost.control.webScraper.WebScraper;
import org.syscall.livingcost.model.LivingCost;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NumbeoLivingCostFeederTest {
    @Mock
    private WebScraper scraperMock;

    private NumbeoLivingCostFeeder feeder;
    private String htmlContent;

    @BeforeEach
    void setUp() throws Exception{
        try (InputStream is = getClass().getResourceAsStream("/spain-numbeo.html")) {
            assertNotNull(is, "Didn't find resource file.");
            htmlContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        NumbeoHtmlExtractor extractor = new NumbeoHtmlExtractor();
        feeder = new NumbeoLivingCostFeeder(scraperMock, extractor);
    }

    @Test
    void testFullFlowRealData() {
        when(scraperMock.getHtmlContent(anyString())).thenReturn(htmlContent);
        LivingCost result = feeder.feed("Spain");

        assertAll("Verification of final integration",
                () -> assertEquals("Spain", result.country()),
                () -> assertEquals("EUR", result.currency()),

                () -> assertEquals(2.08, result.cappuccino(), 0.001),
                () -> assertEquals(1.52, result.gasoline(), 0.001),
                () -> assertEquals(1766.57, result.salaryMonth(), 0.001),

                () -> assertEquals(LocalDate.now(), result.date(), "Date should be today."),
                () -> assertNotNull(result.time(), "Hour should be not null.")
        );
    }
}
