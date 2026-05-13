package syscall.livingcost.control.database;

import syscall.livingcost.model.LivingCost;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class PersistenceMapper {

    public static LivingCost fromMap(Map<String, Object> map) {
        return new LivingCost(
                (String) map.get("country"),
                (String) map.get("currency"),
                extractDouble(map.get("cappuccino")),
                extractDouble(map.get("milk")),
                extractDouble(map.get("rice")),
                extractDouble(map.get("bread")),
                extractDouble(map.get("eggs")),
                extractDouble(map.get("cheese")),
                extractDouble(map.get("chicken")),
                extractDouble(map.get("beef")),
                extractDouble(map.get("fruits")),
                extractDouble(map.get("vegetables")),
                extractDouble(map.get("water")),
                extractDouble(map.get("public_transport")),
                extractDouble(map.get("gasoline")),
                extractDouble(map.get("car")),
                extractDouble(map.get("utilities")),
                extractDouble(map.get("child_care")),
                extractDouble(map.get("gym_monthly")),
                extractDouble(map.get("bedroom_month")),
                extractDouble(map.get("apartment_month")),
                extractDouble(map.get("apartment_buy")),
                extractDouble(map.get("salary_month")),
                extractDouble(map.get("interest_rate"))
        );
    }

    public static double extractDouble(Object value) {
        if (value instanceof Double) return (Double) value;
        if (value instanceof Number) return ((Number) value).doubleValue();
        return 0.0;
    }
}
