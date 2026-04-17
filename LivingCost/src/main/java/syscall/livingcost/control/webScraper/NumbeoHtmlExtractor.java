package syscall.livingcost.control.webScraper;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.List;

public class NumbeoHtmlExtractor {
    private static final List<String> FRUIT_ITEMS = List.of(
            "Apples (1 kg)",
            "Bananas (1 kg)",
            "Oranges (1 kg)",
            "Tomatoes (1 kg)"
    );

    private static final List<String> VEGETABLE_ITEMS = List.of(
            "Potatoes (1 kg)",
            "Onions (1 kg)",
            "Lettuce (1 Head)"
    );

    private static final List<String> UTILITIES_ITEMS = List.of(
            "Basic Utilities for 85 m2 Apartment (Electricity, Heating, Cooling, Water, Garbage)",
            "Mobile Phone Plan (Monthly, with Calls and 10GB+ Data)",
            "Broadband Internet (Unlimited Data, 60 Mbps or Higher)"
    );

    public NumbeoLivingCostData extract(String page) {
        if (page == null) {
            throw new IllegalArgumentException("El documento HTML no puede ser null");
        }
        Document html = Jsoup.parse(page);
        validateDocument(html);

        String currency = extractCurrency(html);
        double cappuccino = extractPriceByName(html, "Cappuccino (Regular Size)");
        double milk = extractPriceByName(html, "Milk (Regular, 1 Liter)");
        double bread = extractPriceByName(html, "Fresh White Bread (500 g Loaf)");
        double rice = extractPriceByName(html, "White Rice (1 kg)");
        double eggs = extractPriceByName(html, "Eggs (12, Large Size)");
        double cheese = extractPriceByName(html, "Local Cheese (1 kg)");
        double chicken = extractPriceByName(html, "Chicken Fillets (1 kg)");
        double beef = extractPriceByName(html, "Beef Round or Equivalent Back Leg Red Meat (1 kg)");
        double fruits = sumPricesByNames(html, FRUIT_ITEMS);
        double vegetables = sumPricesByNames(html, VEGETABLE_ITEMS);
        double water = extractPriceByName(html, "Bottled Water (1.5 Liter)");
        double publicTransport = extractPriceByName(html, "Monthly Public Transport Pass (Regular Price)");
        double gasoline = extractPriceByName(html, "Gasoline (1 Liter)");
        double car = extractPriceByName(html, "Volkswagen Golf 1.5 (or Equivalent New Compact Car)");
        double utilities = sumPricesByNames(html,  UTILITIES_ITEMS);
        double childCare = extractPriceByName(html, "International Primary School, Annual Tuition per Child");
        double gymMonthly = extractPriceByName(html, "Monthly Fitness Club Membership");
        double bedroomMonth = extractPriceByName(html, "1 Bedroom Apartment in City Centre");
        double apartmentMonth = extractPriceByName(html, "3 Bedroom Apartment in City Centre");
        double apartmentBuy = extractPriceByName(html, "Price per Square Meter to Buy Apartment in City Centre");
        double salaryMonth = extractPriceByName(html, "Average Monthly Net Salary (After Tax)");
        double interestRateTwentyYears = extractPriceByName(html, "Annual Mortgage Interest Rate (20-Year Fixed, in %)");

        return new NumbeoLivingCostData(
                currency,
                cappuccino,
                milk,
                bread,
                rice,
                eggs,
                cheese,
                chicken,
                beef,
                fruits,
                vegetables,
                water,
                publicTransport,
                gasoline,
                car,
                utilities,
                childCare,
                gymMonthly,
                bedroomMonth,
                apartmentMonth,
                apartmentBuy,
                salaryMonth,
                interestRateTwentyYears
        );
    }

    private void validateDocument(Document html) {
        if (html == null) {
            throw new IllegalArgumentException("El documento HTML no puede ser null");
        }
    }

    private double extractPriceByName(Document html, String name) {
        Element targetRow = html.selectFirst("tr:contains(" + name + ")");
        if(targetRow == null) {
            return 0.0;
        }

        Element priceCell = targetRow.selectFirst("td.priceValue");
        if (priceCell == null) {
            return 0.0;
        }

        return NumbeoPriceParser.parsePrice(priceCell.text());
    }

    private String extractCurrency(Document html) {
        Element selectedOption = html.selectFirst("#displayCurrency option[selected]");
        if (selectedOption != null) {
            String currency = selectedOption.val();
            return !currency.isEmpty() ? currency : "NOT FOUND";
        }

        return "NOT FOUND";
    }

    private double sumPricesByNames(Document html, List<String> labels) {
        double total = 0.0;
        for (String label : labels) {
            total += extractPriceByName(html, label);
        }
        return total;
    }
}
