package syscall.livingcost.control.event;

import syscall.livingcost.model.LivingCost;
import java.time.Instant;

public record LivingCostEvent(
        String ts,
        String ss,
        LivingCost data
)
{
    public static LivingCostEvent from(String source, LivingCost data) {
        return new LivingCostEvent(Instant.now().toString(), source, data);
    }
}
