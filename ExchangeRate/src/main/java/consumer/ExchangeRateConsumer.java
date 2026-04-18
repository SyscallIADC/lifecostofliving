package consumer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import models.ExchangeRate;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExchangeRateConsumer implements Consumer<ExchangeRate> {
    private static final String BASE_URL = "https://www.alphavantage.co/query";
    private final String apiKey;


    public ExchangeRateConsumer() {
        this.apiKey = System.getenv("ALPHAVANTAGE_API_KEY");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new RuntimeException("API key no encontrada. Define la variable de entorno ALPHAVANTAGE_API_KEY");
        }
    }

    @Override
    public ExchangeRate extractData(String fromCurrency, String toCurrency) throws Exception{
        String urlStr = String.format(
                "%s?function=CURRENCY_EXCHANGE_RATE&from_currency=%s&to_currency=%s&apikey=%s",
                BASE_URL, fromCurrency, toCurrency, apiKey
        );

        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Error HTTP: " + responseCode);
        }

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(reader, JsonObject.class);

            JsonObject data = jsonResponse.getAsJsonObject("Realtime Currency Exchange Rate");
            if (data == null || data.size() == 0) {
                throw new RuntimeException("Respuesta inválida de la API");
            }

            return new ExchangeRate(
                    data.get("1. From_Currency Code").getAsString(),
                    data.get("3. To_Currency Code").getAsString(),
                    data.get("5. Exchange Rate").getAsDouble(),
                    data.get("6. Last Refreshed").getAsString(),
                    data.get("7. Time Zone").getAsString()
            );
        } finally {
            connection.disconnect();
        }
    }

}
