package org.syscall.exchangerate.control.database;

import org.syscall.exchangerate.models.ExchangeRate;
import java.util.List;

public interface ExchangeRateStore {
    void insertData(ExchangeRate exchangeRate);
    List<ExchangeRate> retrieveDataByDate(String date);
    ExchangeRate retrieveLastData();
}

