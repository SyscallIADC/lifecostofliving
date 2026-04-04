package consumer;
import com.microsoft.playwright.*;
import models.LivingCost;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class WebScrapper implements AutoCloseable{
    private static final String URL = "https://www.numbeo.com/cost-of-living/country_result.jsp?country=";
    private static final String STANDARD_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private final Playwright playwright;
    private final Browser browser;
    private final Random randomGenerator;

    public WebScrapper(){
        this.playwright = Playwright.create();
        this.browser = launchHeadlessBrowser();
        this.randomGenerator = new Random();
    }

    public LivingCost extractDataForCountry(String countryName) {
        try (BrowserContext context = createAnonymousContext();
             Page page = context.newPage()) {

            navigateToCountryUrl(page, countryName);
            simulateHumanReadingTime(page);

            return buildLivingCostModel(page, countryName);
        }
    }

    private Browser launchHeadlessBrowser() {
        return playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
    }

    private BrowserContext createAnonymousContext() {
        return browser.newContext(
                new Browser.NewContextOptions()
                        .setUserAgent(STANDARD_USER_AGENT)
                        .setViewportSize(1920,1080)
        );
    }

    private void simulateHumanReadingTime(Page page) {
        int randomDelayInMilliseconds = 3000 + randomGenerator.nextInt(4000);
        page.waitForTimeout(randomDelayInMilliseconds);
    }

    private void navigateToCountryUrl(Page page, String countryName) {
        String countryUrl = countryName.replace(" ", "+");
        String targetUrl = URL + countryName;
        page.navigate(targetUrl);
        page.waitForLoadState();
    }

    private LivingCost buildLivingCostModel(Page page, String countryName) {
        String currency = extractCurrency(page);
        double fruits = extractFruits(page);
        double vegetables = extractVegetables(page);
        double utilities = extractUtilities(page);

        return new LivingCost(
                countryName,
                currency,
                extractPriceByName(page, "Cappuccino (Regular Size)"),
                extractPriceByName(page, "Milk (Regular, 1 Liter)"),
                extractPriceByName(page, "Fresh White Bread (500 g Loaf)"),
                extractPriceByName(page, "White Rice (1 kg)"),
                extractPriceByName(page, "Eggs (12, Large Size)"),
                extractPriceByName(page, "Local Cheese (1 kg)"),
                extractPriceByName(page, "Chicken Fillets (1 kg)"),
                extractPriceByName(page, "Beef Round or Equivalent Back Leg Red Meat (1 kg)"),
                fruits,
                vegetables,
                extractPriceByName(page, "Bottled Water (1.5 Liter)"),
                extractPriceByName(page, "Monthly Public Transport Pass (Regular Price)"),
                extractPriceByName(page, "Gasoline (1 Liter)"),
                extractPriceByName(page, "Volkswagen Golf 1.5 (or Equivalent New Compact Car)"),
                utilities,
                extractPriceByName(page, "Monthly Fitness Club Membership"),
                extractPriceByName(page, "International Primary School, Annual Tuition per Child"),
                extractPriceByName(page, "1 Bedroom Apartment in City Centre"),
                extractPriceByName(page, "3 Bedroom Apartment in City Centre"),
                extractPriceByName(page, "Price per Square Meter to Buy Apartment in City Centre"),
                extractPriceByName(page, "Average Monthly Net Salary (After Tax)"),
                extractPriceByName(page, "Annual Mortgage Interest Rate (20-Year Fixed, in %)"),
                LocalDate.now(),
                LocalTime.now()
        );
    }

    private String extractCurrency(Page page) {
        Locator currencyForm = page.locator("#displayCurrency");
        return currencyForm.isVisible() ? currencyForm.inputValue() : "NOT FOUND";
    }
    private double extractPriceByName(Page page, String name) {
        Locator targetRow = page.locator("tr").filter(
                new Locator.FilterOptions().setHasText(name)
        ).first();

        Locator priceCell = targetRow.locator("td.priceValue").first();

        if(priceCell.isVisible()) {
            return parsePrice(priceCell.innerText());
        }
        return 0.0;
    }

    private double parsePrice(String rawPrice) {
        String numericText = rawPrice.replaceAll("[^\\d.]", "").replace(",", "");
        if (numericText.isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(numericText);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private double extractFruits(Page page) {
        Double totalPrice = 0.0;
        List<String> fruits = List.of(
                "Apples (1 kg)",
                "Bananas (1 kg)",
                "Oranges (1 kg)",
                "Tomatoes (1 kg)"
        );
        for (String fruit : fruits) {
            Double fruitPrice = extractPriceByName(page, fruit);
            totalPrice += fruitPrice;
        }
        return totalPrice;
    }

    private double extractVegetables(Page page) {
        Double totalPrice = 0.0;
        List<String> vegetables = List.of(
                "Potatoes (1 kg)",
                "Onions (1 kg)",
                "Lettuce (1 Head)"
        );
        for (String vegetable : vegetables) {
            Double vegetablePrice = extractPriceByName(page, vegetable);
            totalPrice += vegetablePrice;
        }
        return totalPrice;
    }

    private double extractUtilities(Page page) {
        Double totalPrice = 0.0;
        List<String> utilities = List.of(
                "Basic Utilities for 85 m2 Apartment (Electricity, Heating, Cooling, Water, Garbage)",
                "Mobile Phone Plan (Monthly, with Calls and 10GB+ Data)",
                "Broadband Internet (Unlimited Data, 60 Mbps or Higher)"
        );
        for (String utility : utilities) {
            Double utilityPrice = extractPriceByName(page, utility);
            totalPrice += utilityPrice;
        }
        return totalPrice;
    }

    @Override
    public void close() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
