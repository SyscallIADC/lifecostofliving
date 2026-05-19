package org.syscall.livingcost.control.feeder;

import org.syscall.livingcost.model.LivingCost;

public interface LivingCostFeeder {
    LivingCost feed(String countryName);
}
