package database;

import java.util.List;

public interface DatabaseHelper <Model>{
    void insertData(Model model);
    List<Model> retrieveDataByDate(String date);
    Model retrieveLastData();



}
