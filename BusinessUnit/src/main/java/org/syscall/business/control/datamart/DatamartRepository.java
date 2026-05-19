package org.syscall.business.control.datamart;

import org.syscall.business.model.CountryStats;

import java.util.List;

public interface DatamartRepository {
    void upsertCountryData(ParsedEvent event);
    void upsertExchangeRateData(ParsedEvent event);
    List<CountryStats> getCountryHistory(String countryName, int limit);
    List<Double> getExchangeRateHistory(String fromCurrency, String toCurrency, int limit);
    CountryStats getCountryStats(String countryName);
    List<CountryStats> getAllCountries();
    double getExchangeRate(String fromCurrency, String toCurrency);
}