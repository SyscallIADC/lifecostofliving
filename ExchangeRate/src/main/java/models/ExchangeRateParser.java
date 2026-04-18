package models;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRateParser implements ModelParser<ExchangeRate> {

    @Override
    public Map<String, Object> toMap(ExchangeRate rate) {
        Map<String, Object> map = new HashMap<>();
        map.put("from_currency", rate.getFromCurrency());
        map.put("to_currency", rate.getToCurrency());
        map.put("exchange_rate", rate.getExchangeRate());
        map.put("last_refreshed", rate.getLastRefreshed());
        map.put("time_zone", rate.getTimeZone());
        return map;
    }

    @Override
    public ExchangeRate fromMap(Map<String, Object> map) {
        return new ExchangeRate(
                (String) map.get("from_currency"),
                (String) map.get("to_currency"),
                extractDouble(map.get("exchange_rate")),
                (String) map.get("last_refreshed"),
                (String) map.get("time_zone")
        );
    }

    private static double extractDouble(Object value) {
        if (value instanceof Number) return ((Number) value).doubleValue();
        return 0.0;
    }
}