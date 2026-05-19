package org.syscall.livingcost.control.database;

import org.syscall.livingcost.model.LivingCost;

import java.util.List;

public interface LivingCostStore {
    void insertData(LivingCost livingCost);
    List<LivingCost> retrieveDataByDate(String date);
    LivingCost retrieveLastData();
}
