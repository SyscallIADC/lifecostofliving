package syscall.livingcost.control.database;

import syscall.livingcost.model.LivingCost;

import java.util.List;

public interface LivingCostStore {
    void insertData(LivingCost livingCost);
    List<LivingCost> retrieveDataByDate(String date);
    LivingCost retrieveLastData();
}
