package feeder;

import models.ExchangeRate;

public interface ExchangeRateFeeder {
    ExchangeRate feed(String fromCurrency, String toCurrency) throws Exception;
}

