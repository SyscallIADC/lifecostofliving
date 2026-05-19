package org.syscall.business.control.analysis;

import org.syscall.business.control.datamart.DatamartRepository;
import org.syscall.business.model.CountryStats;
import org.syscall.business.model.MarketTrend;

import java.util.List;

import static java.lang.Math.abs;

public class TrendAnalyzer {
    private final DatamartRepository datamart;

    public TrendAnalyzer(DatamartRepository datamart) {
        this.datamart = datamart;
    }

    public MarketTrend analyze(String countryName, String currency) {
        List<CountryStats> countryHistory = datamart.getCountryHistory(countryName, 100);
        List<Double> currencyHistory = datamart.getExchangeRateHistory(currency, "EUR", 60);

        MarketTrend countryTrend = calculateCountryTrend(countryHistory);
        MarketTrend currencyTrend = calculateCurrencyTrend(currencyHistory);

        if (countryTrend == MarketTrend.VOLATILE || currencyTrend == MarketTrend.VOLATILE) {
            return MarketTrend.VOLATILE;
        }
        if (countryTrend == MarketTrend.INFLATION || currencyTrend == MarketTrend.INFLATION) {
            return MarketTrend.INFLATION;
        }
        if (countryTrend == MarketTrend.DEFLATION || currencyTrend == MarketTrend.DEFLATION) {
            return MarketTrend.DEFLATION;
        }

        return MarketTrend.STABLE;
    }

    private MarketTrend calculateCountryTrend(List<CountryStats> history) {
        if (history == null || history.size() < 2) return MarketTrend.STABLE;

        double newestCost = history.get(0).costOptimum();
        double oldestCost = history.get(history.size() - 1).costOptimum();

        if (oldestCost == 0) return MarketTrend.STABLE;

        double percentageChange = ((newestCost - oldestCost) / oldestCost) * 100.0;
        double cv = calculateCoefficientOfVariation(
                history.stream().mapToDouble(CountryStats::costOptimum).toArray()
        );

        return categorizeTrend(percentageChange, cv);
    }

    private MarketTrend calculateCurrencyTrend(List<Double> history) {
        if (history == null || history.size() < 2) return MarketTrend.STABLE;

        double newestRate = history.get(0);
        double oldestRate = history.get(history.size() - 1);

        if (oldestRate == 0) return MarketTrend.STABLE;

        double percentageChange = ((newestRate - oldestRate) / oldestRate) * 100.0;
        double cv = calculateCoefficientOfVariation(
                history.stream().mapToDouble(Double::doubleValue).toArray()
        );

        return categorizeTrend(percentageChange, cv);
    }

    private double calculateCoefficientOfVariation(double[] values) {
        double sum = 0;
        for (double v : values) sum += v;
        double mean = sum / values.length;

        double varianceSum = 0;
        for (double v : values) varianceSum += Math.pow(v - mean, 2);
        double variance = varianceSum / values.length;

        return (Math.sqrt(variance) / mean) * 100.0;
    }

    private MarketTrend categorizeTrend(double percentageChange, double cv) {
        if (abs(cv) > 12.0) return MarketTrend.VOLATILE;
        if (percentageChange > 5.0) return MarketTrend.INFLATION;
        if (percentageChange < -5.0) return MarketTrend.DEFLATION;
        return MarketTrend.STABLE;
    }
}