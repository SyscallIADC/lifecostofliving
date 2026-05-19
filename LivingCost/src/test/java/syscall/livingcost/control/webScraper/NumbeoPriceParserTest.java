package syscall.livingcost.control.webScraper;

import org.junit.jupiter.api.Test;
import org.syscall.livingcost.control.webScraper.NumbeoPriceParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumbeoPriceParserTest {
    @Test
    void testNull() {
        double test = NumbeoPriceParser.parsePrice(null);
        assertEquals(0.0, test);
    }

    @Test
    void testOnlyCommas(){
        double test = NumbeoPriceParser.parsePrice("1,2,3");
        assertEquals(123, test);
    }

    @Test
    void testOnlyDots() {
        double test = NumbeoPriceParser.parsePrice("1.2.3");
        assertEquals(1.2, test);
    }

    @Test
    void testDotsCommas() {
        double test = NumbeoPriceParser.parsePrice("1,234.21");
        assertEquals(1234.21, test);
    }

    @Test
    void testWithCurrency() {
        double test = NumbeoPriceParser.parsePrice("20,000.50€");
        assertEquals(20000.5, test);
    }

    @Test
    void testPeru() {
        double test = NumbeoPriceParser.parsePrice("123,456.78 S/.");
        assertEquals(123456.78, test);
    }
}
