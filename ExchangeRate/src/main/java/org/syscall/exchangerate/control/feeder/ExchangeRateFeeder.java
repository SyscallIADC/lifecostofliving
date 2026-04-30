package org.syscall.exchangerate.control.feeder;

import org.syscall.exchangerate.models.ExchangeRate;

public interface ExchangeRateFeeder {
    ExchangeRate feed(String fromCurrency, String toCurrency) throws Exception;
}

