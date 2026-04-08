package models;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalTime;
import java.time.LocalDate;
public class LivingCostParser implements ModelParser<LivingCost> {
    @Override
    public Map<String, Object> toMap(LivingCost livingCost) {
        Map<String, Object> map = new HashMap<>();
        map.put("capuccino", livingCost.getCapuccino());
        map.put("milk", livingCost.getMilk());
        map.put("bread", livingCost.getBread());
        map.put("rice", livingCost.getRice());
        map.put("eggs", livingCost.getEggs());
        map.put("cheese", livingCost.getCheese());
        map.put("chicken", livingCost.getChicken());
        map.put("beef", livingCost.getBeef());
        map.put("fruits", livingCost.getFruits());
        map.put("vegetables", livingCost.getVegetables());
        map.put("water", livingCost.getWater());
        map.put("public_transport", livingCost.getPublicTransport());
        map.put("gasoline", livingCost.getGasoline());
        map.put("car", livingCost.getCar());
        map.put("utilities", livingCost.getUtilities());
        map.put("childCare", livingCost.getChildCare());
        map.put("gym_monthly", livingCost.getGymMonthly());
        map.put("bedroom_month", livingCost.getBedroomMonth());
        map.put("appartment_month", livingCost.getAppartmentMonth());
        map.put("appartment_buy", livingCost.getAppartmentBuy());
        map.put("salary_month", livingCost.getSalaryMonth());
        map.put("interest_rate", livingCost.getInterestRateTwentyYears());
        map.put("country", livingCost.getCountry());
        map.put("currency", livingCost.getCurrency());
        map.put("date", livingCost.getDate().toString());
        map.put("time", livingCost.getTime().toString());
        return map;
    }

    @Override
    public LivingCost fromMap(Map<String, Object> map) {
        return new LivingCost(
                (String) map.get("country"),
                (String) map.get("currency"),
                extractDouble(map.get("capuccino")),
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
                extractDouble(map.get("childCare")),
                extractDouble(map.get("gym_monthly")),
                extractDouble(map.get("bedroom_month")),
                extractDouble(map.get("appartment_month")),
                extractDouble(map.get("appartment_buy")),
                extractDouble(map.get("salary_month")),
                extractDouble(map.get("interest_rate")),
                map.get("date") instanceof String ? LocalDate.parse((String) map.get("date")) : (LocalDate) map.get("date"),
                map.get("time") instanceof String ? LocalTime.parse((String) map.get("time")) : (LocalTime) map.get("time")
        );
    }

    private double extractDouble(Object value) {
        if (value instanceof Double) return (Double) value;
        if (value instanceof Number) return ((Number) value).doubleValue();
        return 0.0;
    }

}
