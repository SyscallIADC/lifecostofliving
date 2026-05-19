package org.syscall.control.feeder;

public interface ExchangeRateFeeder {
    void feed(String fromCurrency, String toCurrency) throws Exception;
}