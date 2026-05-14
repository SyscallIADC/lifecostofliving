package org.syscall.models;

public class ExchangeRate {
    private String fromCurrency;
    private String toCurrency;
    private double exchangeRate;
    private String lastRefreshed;
    private String timeZone;

    public ExchangeRate(String fromCurrency, String toCurrency, double exchangeRate, String lastRefreshed, String timeZone) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.exchangeRate = exchangeRate;
        this.lastRefreshed = lastRefreshed;
        this.timeZone = timeZone;
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