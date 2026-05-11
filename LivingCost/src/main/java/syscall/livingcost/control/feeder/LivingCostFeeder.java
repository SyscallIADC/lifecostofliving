package syscall.livingcost.control.feeder;

import syscall.livingcost.model.LivingCost;

public interface LivingCostFeeder {
    LivingCost feed(String countryName);
}
