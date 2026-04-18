package models;

public class ExchangeRate {
    private final String fromCurrency;
    private final String toCurrency;
    private final double exchangeRate;
    private final String lastRefreshed;
    private final String timeZone;

    public ExchangeRate(String fromCurrency, String toCurrency, double exchangeRate, String lastRefreshed, String timeZone) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.exchangeRate = exchangeRate;
        this.lastRefreshed = lastRefreshed;
        this.timeZone = timeZone;
    }

    public String getFromCurrency() { return fromCurrency; }
    public String getToCurrency() { return toCurrency; }
    public double getExchangeRate() { return exchangeRate; }
    public String getLastRefreshed() { return lastRefreshed; }
    public String getTimeZone() { return timeZone; }

    @Override
    public String toString() {
        return fromCurrency + " -> " + toCurrency + ": " + exchangeRate;
    }
}