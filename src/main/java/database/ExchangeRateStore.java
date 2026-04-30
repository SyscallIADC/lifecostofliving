package database;

import java.util.List;

public interface ExchangeRateStore<Model>{
    void insertData(Model model);
    List<Model> retrieveDataByDate(String date);
    Model retrieveLastData();



}
