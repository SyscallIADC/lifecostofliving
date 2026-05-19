package org.syscall.business.control;

import org.syscall.business.control.analysis.TrendAnalyzer;
import org.syscall.business.control.datamart.DatamartRepository;
import org.syscall.business.model.CountryStats;
import org.syscall.business.model.MarketTrend;

import java.util.Comparator;
import java.util.List;

public class BusinessController {
    private final DatamartRepository datamart;
    private final TrendAnalyzer trendAnalyzer;

    public BusinessController(DatamartRepository datamart, TrendAnalyzer trendAnalyzer) {
        this.datamart = datamart;
        this.trendAnalyzer = trendAnalyzer;
    }

    public double convertToEuros(String currency) {
        return datamart.getExchangeRate(currency, "EUR");
    }

    public CountryStats getCountryDetails(String countryName) {
        return datamart.getCountryStats(countryName);
    }

    public MarketTrend getMarketTrend(String countryName, String currency) {
        return trendAnalyzer.analyze(countryName, currency);
    }

    public List<CountryStats> getTopViableCountries(double salaryInEur) {
        return datamart.getAllCountries().stream()
                .filter(s -> salaryInEur > s.costOptimum())
                .sorted(Comparator.comparingDouble((CountryStats s) -> salaryInEur - s.costOptimum()).reversed())
                .limit(10)
                .toList();
    }
}