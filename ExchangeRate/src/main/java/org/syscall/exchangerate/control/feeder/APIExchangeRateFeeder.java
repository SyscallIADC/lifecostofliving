package org.syscall.exchangerate.control.feeder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.syscall.exchangerate.models.ExchangeRate;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIExchangeRateFeeder implements ExchangeRateFeeder {

    private static final String BASE_URL = "https://www.alphavantage.co/query";
    private final String apiKey;

    public APIExchangeRateFeeder() {
        this.apiKey = System.getenv("ALPHAVANTAGE_API_KEY");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new RuntimeException("API key no encontrada. Define la variable de entorno ALPHAVANTAGE_API_KEY");
        }
    }

    @Override
    public ExchangeRate feed(String fromCurrency, String toCurrency) throws Exception {
        URL url = new URL(buildUrl(fromCurrency, toCurrency));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Error HTTP: " + connection.getResponseCode());
        }

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            return parseResponse(new Gson().fromJson(reader, JsonObject.class));
        }
    }

    private String buildUrl(String fromCurrency, String toCurrency) {
        return String.format(
                "%s?function=CURRENCY_EXCHANGE_RATE&from_currency=%s&to_currency=%s&apikey=%s",
                BASE_URL, fromCurrency, toCurrency, apiKey
        );
    }

    private ExchangeRate parseResponse(JsonObject json) {
        JsonObject data = json.getAsJsonObject("Realtime Currency Exchange Rate");
        if (data == null) throw new RuntimeException("Respuesta inválida de la API");
        return new ExchangeRate(
                data.get("1. From_Currency Code").getAsString(),
                data.get("3. To_Currency Code").getAsString(),
                data.get("5. Exchange Rate").getAsDouble(),
                data.get("6. Last Refreshed").getAsString(),
                data.get("7. Time Zone").getAsString()
        );
    }
}
