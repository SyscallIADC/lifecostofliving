package models;
import java.util.Map;
import java.util.HashMap;


public class ExchangeRate {

    private String fromCurrency;
    private String toCurrency;
    private double exchangeRate;
    private String lastRefreshed;
    private String timeZone;

    public ExchangeRate(String s) {
    }

    public ExchangeRate(String fromCurrency, String toCurrency, double exchangeRate, String lastRefreshed, String timeZone) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.exchangeRate = exchangeRate;
        this.lastRefreshed = lastRefreshed;
        this.timeZone = timeZone;
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("from-currency", fromCurrency);
        map.put("to-currency", toCurrency);
        map.put("exchange-rate", exchangeRate);
        map.put("last-refreshed", lastRefreshed);
        map.put("time-zone", timeZone);
        return map;
    }

    public static double extractDouble(Object value) {
        if (value instanceof Double) return (Double) value;
        if (value instanceof Number) return ((Number) value).doubleValue();
        return 0.0;
    }

    public static ExchangeRate fromMap(Map<String, Object> map) {
        return new ExchangeRate(
                (String) map.get("from-currency"),
                (String) map.get("to-currency"),
                extractDouble(map.get("exchange-rate")),
                (String) map.get("last-refreshed"),
                (String) map.get("time-zone")
        );
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public String getLastRefreshed() {
        return lastRefreshed;
    }

    public String getTimeZone() {
        return timeZone;
    }
}
