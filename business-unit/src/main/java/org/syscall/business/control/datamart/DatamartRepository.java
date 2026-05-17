package org.syscall.business.control.datamart;

public interface DatamartRepository {
    void upsertCountryData(ParsedEvent event);
    void upsertExchangeRateData(ParsedEvent event);
}