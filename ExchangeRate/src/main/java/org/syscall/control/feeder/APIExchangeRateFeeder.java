package org.syscall.control.feeder;

import com.google.gson.JsonObject;
import org.syscall.control.api.APIConsumer;
import org.syscall.control.publisher.ActiveMQExchangeRatePublisher;
import org.syscall.control.publisher.Serializer;
import org.syscall.models.ExchangeRate;

import javax.jms.JMSException;

public class APIExchangeRateFeeder implements ExchangeRateFeeder {

    private static final String BASE_URL = "https://www.alphavantage.co/query";
    private static final String SOURCE_ID = "ExchangeRate-feeder";

    private final String apiKey;
    private final APIConsumer apiConsumer;
    private final Serializer serializer;
    private final ActiveMQExchangeRatePublisher publisher;

    public APIExchangeRateFeeder(ActiveMQExchangeRatePublisher publisher) {
        this.apiKey = System.getenv("ALPHAVANTAGE_API_KEY");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new RuntimeException("API key no encontrada. Define ALPHAVANTAGE_API_KEY");
        }
        this.apiConsumer = new APIConsumer();
        this.serializer = new Serializer();
        this.publisher = publisher;
    }

    @Override
    public void feed(String fromCurrency, String toCurrency) throws Exception {
        String url = buildUrl(fromCurrency, toCurrency);
        JsonObject response = apiConsumer.fetch(url);
        ExchangeRate rate = parseResponse(response);
        String json = serializer.serialize(rate, SOURCE_ID);
        publisher.send(json);
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