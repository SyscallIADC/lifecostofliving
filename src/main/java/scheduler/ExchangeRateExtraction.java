package scheduler;

import consumer.ApiConsumer;
import database.DatabaseHelper;
import models.ExchangeRate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeRateExtraction {

    private final List<String> currencies = List.of(
            "EUR", "CHF", "GBP", "INR", "RUB", "CNY", "JPY",
            "AUD", "USD", "MXN", "ARS", "PEN", "BRL", "THB",
            "KRW", "AED", "KYD", "DOP", "QAR", "ILS", "ZAR"
    );

    private final ApiConsumer apiConsumer = new ApiConsumer();

    public void execute() {
        System.out.println("Starting exchange rate extraction...");

        for (String currency : currencies) {
            try {
                ExchangeRate rate = apiConsumer.getExchangeRate(currency, "EUR");

                if (rate != null) {
                    DatabaseHelper.getInstance().insertExchangeRate(rate);
                    System.out.println("Saved: " + currency);
                }

                Thread.sleep(12000);

            } catch (Exception e) {
                System.out.println("Error with currency: " + currency);
                e.printStackTrace();
            }
        }

        System.out.println("Finished exchange rate extraction.");
    }
}
