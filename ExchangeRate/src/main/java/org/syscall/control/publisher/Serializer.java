package org.syscall.control.publisher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.syscall.models.ExchangeRate;

import java.time.Instant;

public class Serializer {

    public String serialize(ExchangeRate rate, String sourceId) {
        JsonObject json = new JsonObject();
        json.addProperty("ts", Instant.now().toString());
        json.addProperty("ss", sourceId);
        json.addProperty("from_currency", rate.getFromCurrency());
        json.addProperty("to_currency", rate.getToCurrency());
        json.addProperty("exchange_rate", rate.getExchangeRate());
        json.addProperty("time_zone", rate.getTimeZone());
        return new Gson().toJson(json);
    }
}