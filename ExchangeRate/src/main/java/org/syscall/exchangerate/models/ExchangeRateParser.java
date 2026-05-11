package org.syscall.exchangerate.models;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRateParser implements ModelParser<ExchangeRate>{
    @Override
    public Map<String, Object> toMap(ExchangeRate exchangeRate) {
        Map<String, Object> map = new HashMap<>();
        map.put("from-currency", exchangeRate.getFromCurrency());
        map.put("to-currency", exchangeRate.getToCurrency());
        map.put("exchange-rate", exchangeRate.getExchangeRate());
        map.put("last-refreshed", exchangeRate.getLastRefreshed());
        map.put("time-zone", exchangeRate.getTimeZone());
        return map;
    }

    @Override
    public ExchangeRate fromMap(Map<String, Object> map) {
        return new ExchangeRate(
                (String) map.get("from-currency"),
                (String) map.get("to-currency"),
                extractDouble(map.get("exchange-rate")),
                (String) map.get("last-refreshed"),
                (String) map.get("time-zone")
        );
    }

    public static double extractDouble(Object value) {
        if (value instanceof Double) return (Double) value;
        if (value instanceof Number) return ((Number) value).doubleValue();
        return 0.0;
    }
}
