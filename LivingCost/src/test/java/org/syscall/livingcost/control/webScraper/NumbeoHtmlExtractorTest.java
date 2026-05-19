package org.syscall.livingcost.control.webScraper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.syscall.livingcost.control.webScraper.NumbeoHtmlExtractor;
import org.syscall.livingcost.control.webScraper.NumbeoLivingCostData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class NumbeoHtmlExtractorTest {
    private NumbeoHtmlExtractor extractor;

    @BeforeEach
    public void setup() {
        extractor = new NumbeoHtmlExtractor();
    }

    @Test
    public void testExtractDataFromRealHtml() throws IOException {
        Path htmlPath = Path.of("src/test/resources/spain-numbeo.html");
        String htmlContent = Files.readString(htmlPath);

        NumbeoLivingCostData result = extractor.extract(htmlContent);

        assertAll("Numbeo data extraction validation",
                () -> assertEquals("EUR", result.currency(), "La moneda debe ser EUR"),
                () -> assertEquals(2.08, result.cappuccino(), 0.001, "Error en Cappuccino"),
                () -> assertEquals(1.05, result.milk(), 0.001, "Error en Milk"),
                () -> assertEquals(1.43, result.rice(), 0.001, "Error en Rice"),
                () -> assertEquals(12.51, result.cheese(), 0.001, "Error en Cheese"),
                () -> assertEquals(0.67, result.water(), 0.001, "Error en Water"),
                () -> assertEquals(30.00, result.publicTransport(), 0.001, "Error en Public Transport"),
                () -> assertEquals(1.52, result.gasoline(), 0.001, "Error en Gasoline"),
                () -> assertEquals(31500.00, result.car(), 0.001, "Error en Car"),
                () -> assertEquals(9278.20, result.childCare(), 0.001, "Error en Childcare"),
                () -> assertEquals(40.45, result.gymMonthly(), 0.001, "Error en Gym"),
                () -> assertEquals(892.26, result.bedroomMonth(), 0.001, "Error en 1 Bedroom"),
                () -> assertEquals(1421.27, result.apartmentMonth(), 0.001, "Error en 3 Bedroom"),
                () -> assertEquals(3789.78, result.apartmentBuy(), 0.001, "Error en Apartment Buy"),
                () -> assertEquals(1766.57, result.salaryMonth(), 0.001, "Error en Salary"),
                () -> assertEquals(3.34, result.interestRate(), 0.001, "Error en Interest Rate"),
                () -> assertEquals(8.07, result.fruits(), 0.001, "Error en suma de Frutas"),
                () -> assertEquals(4.53, result.vegetables(), 0.001, "Error en suma de Verduras"),
                () -> assertEquals(178.52, result.utilities(), 0.001, "Error en suma de Utilidades")
        );
    }

    @Test
    void testThrowExceptionWhenNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> extractor.extract(null)
        );
        assertEquals("El documento HTML no puede ser null", exception.getMessage());
    }

    @Test
    void testReturnDefaultHtmlEmpty() {
        String emptyHtml = "<html><body></body></html>";

        NumbeoLivingCostData result = extractor.extract(emptyHtml);

        assertEquals("NOT FOUND", result.currency());
        assertEquals(0.0, result.cappuccino(), 0.001);
        assertEquals(0.0, result.fruits(), 0.001);
    }
}
