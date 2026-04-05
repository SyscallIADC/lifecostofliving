package scheduler;

import database.DatabaseHelper;
import models.ExchangeRate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Component
public class ExchangeRateExtraction {

    private final List<String> currencies = List.of(
            "EUR", "CHF", "GBP", "INR", "RUB", "CNY", "JPY",
            "AUD", "USD", "MXN", "ARS", "PEN", "BRL", "THB",
            "KRW", "AED", "KYD", "DOP", "QAR", "ILS", "ZAR"
    );

    private final String API_KEY = "TU_API_KEY";

    public void execute() {
        System.out.println("Starting exchange rate extraction...");

        for (String currency : currencies) {
            try {
                ExchangeRate rate = fetchExchangeRate(currency);

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

    private ExchangeRate fetchExchangeRate(String fromCurrency) {
        try {
            String urlStr = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE"
                    + "&from_currency=" + fromCurrency
                    + "&to_currency=EUR"
                    + "&apikey=" + API_KEY;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            return parseResponse(response.toString());

        } catch (Exception e) {
            System.out.println("API call failed for " + fromCurrency);
            return null;
        }
    }

    private ExchangeRate parseResponse(String json) {
        try {
            String from = extract(json, "\"1. From_Currency Code\": \"", "\"");
            String to = extract(json, "\"3. To_Currency Code\": \"", "\"");
            String rateStr = extract(json, "\"5. Exchange Rate\": \"", "\"");
            String lastRefreshed = extract(json, "\"6. Last Refreshed\": \"", "\"");
            String timeZone = extract(json, "\"7. Time Zone\": \"", "\"");

            double rate = Double.parseDouble(rateStr);

            return new ExchangeRate(from, to, rate, lastRefreshed, timeZone);

        } catch (Exception e) {
            System.out.println("Error parsing JSON");
            return null;
        }
    }

    private String extract(String json, String start, String end) {
        int i = json.indexOf(start);
        if (i == -1) return "";
        i += start.length();
        int j = json.indexOf(end, i);
        return json.substring(i, j);
    }
}