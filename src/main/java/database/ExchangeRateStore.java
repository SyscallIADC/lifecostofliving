package database;

import models.ExchangeRate;
import java.util.List;

public interface ExchangeRateStore {
    void insertData(ExchangeRate exchangeRate);
    List<ExchangeRate> retrieveDataByDate(String date);
    ExchangeRate retrieveLastData();
}

