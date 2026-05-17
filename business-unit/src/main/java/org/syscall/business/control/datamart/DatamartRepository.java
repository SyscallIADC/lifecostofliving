package org.syscall.business.control.datamart;

import org.syscall.business.model.CountryStats;

import java.util.List;

public interface DatamartRepository {
    void upsertCountryData(ParsedEvent event);
    void upsertExchangeRateData(ParsedEvent event);
    CountryStats getCountryStats(String country);
    List<CountryStats> getAllCountries();
    double getExchangeRate(String fromCurrency, String toCurrency);
}